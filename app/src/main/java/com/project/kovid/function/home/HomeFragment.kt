package com.project.kovid.function.home

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.google.android.material.tabs.TabLayout
import com.project.kovid.R
import com.project.kovid.base.BaseFragment
import com.project.kovid.databinding.FragmentHomeBinding
import com.project.kovid.model.WeekCovid
import com.project.kovid.util.StringUtil

class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home) {
    //private val viewModel: HomeViewModel by activityViewModels() //activity 의 ViewModel 을 따름
    private val homeViewModel: HomeViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = homeViewModel
        binding.lifecycleOwner = this

        homeViewModel.getChartData()
        homeViewModel.getAreaData()

        chartInit()


        subscribe(this)
    }

    private fun subscribe(owner: LifecycleOwner) {

    }

    private fun chartInit(){
        binding.chart.apply {
            setOnChartValueSelectedListener(valueOnSelectedListener)
            setOnTouchListener(chartOnTouchListener)
        }
        binding.chartTabLayout.addOnTabSelectedListener(tabOnTabSelectedListener)
    }

    fun areaRecycleInit(){
        val linearLayoutManager = LinearLayoutManager(mContext)
        binding.areaDecideRecycler.apply {
            layoutManager = linearLayoutManager
            setHasFixedSize(true)


            withModels {

            }
        }
    }


    private val valueOnSelectedListener = object : OnChartValueSelectedListener{
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

    private val tabOnTabSelectedListener = object :TabLayout.OnTabSelectedListener{
        override fun onTabSelected(tab: TabLayout.Tab?) {
            Log.d("탭온 클릭", "${tab?.position}")
            when(tab?.position){
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

