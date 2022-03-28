package com.project.kovid.function.home

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.google.android.material.tabs.TabLayout
import com.project.kovid.R
import com.project.kovid.databinding.FragmentHomeBinding
import com.project.kovid.base.BaseFragment
import com.project.kovid.model.WeekCovid
import com.project.kovid.util.StringUtil

class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home) {
    lateinit var homeViewModel: HomeViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        homeViewModel = ViewModelProvider(requireActivity())[HomeViewModel::class.java]

        binding.viewModel = homeViewModel
        binding.lifecycleOwner = this

        chartInit()

        subscribe(this)
    }

    override fun onStart() {
        super.onStart()
        homeViewModel.getChartData()
        homeViewModel.getAreaData()
    }

    private fun chartInit() {
        binding.chart.apply {
            setOnChartValueSelectedListener(valueOnSelectedListener)
            setOnTouchListener(chartOnTouchListener)
        }
        binding.chartTabLayout.addOnTabSelectedListener(tabOnTabSelectedListener)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun subscribe(owner: LifecycleOwner) {
        homeViewModel.areaDecide.observe(owner) {
            binding.areaViewPager.apply {
                adapter = ViewPagerAdapter(it)
                setOnTouchListener(chartOnTouchListener)
                (getChildAt(0) as RecyclerView).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
            }
            binding.wormDotsIndicator.setViewPager2(binding.areaViewPager)
        }
    }


    private val valueOnSelectedListener = object : OnChartValueSelectedListener {
        override fun onValueSelected(e: Entry?, h: Highlight?) {
            e.let {
                val weekCovid = e?.data as WeekCovid
                homeViewModel.topDecideDate.value = weekCovid.day
                homeViewModel.topDecide.value = StringUtil.getDecimalFormatNum(weekCovid.decideCnt)
                Log.d("타입", weekCovid.day)
            }
        }

        override fun onNothingSelected() {}
    }

    private val tabOnTabSelectedListener = object : TabLayout.OnTabSelectedListener {
        override fun onTabSelected(tab: TabLayout.Tab?) {
            Log.d("탭온 클릭", "${tab?.position}")
            when (tab?.position) {
                0 -> homeViewModel.weekDataSet()
                1 -> homeViewModel.monthDataSet()
            }
        }

        override fun onTabUnselected(tab: TabLayout.Tab?) {}
        override fun onTabReselected(tab: TabLayout.Tab?) {}
    }

    private val chartOnTouchListener = View.OnTouchListener { v, event ->
        when (event!!.action) {
            MotionEvent.ACTION_DOWN -> {
                binding.homeNestedScroll.requestDisallowInterceptTouchEvent(true)
                v.performClick()
            }
            MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> {
                binding.homeNestedScroll.requestDisallowInterceptTouchEvent(false)
            }
        }
        false
    }

    private fun lightDarkThemeCheck(): Int {
        return if (requireActivity().resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES) {
            ContextCompat.getColor(mContext, R.color.white)
        } else {
            ContextCompat.getColor(mContext, R.color.black)
        }
    }
}

