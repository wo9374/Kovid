package com.project.kovid.function.home

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.listener.ChartTouchListener
import com.github.mikephil.charting.listener.OnChartGestureListener
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
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

        homeViewModel.uiModeColor.value = lightDarkThemeCheck()
        homeViewModel.getWeekCovid()

        binding.chart.apply {
            setNoDataText(getString(R.string.data_loading))  //data 없을때 표시 text
            setOnChartValueSelectedListener(valueOnSelectedListener)
        }

        binding.chartTabLayout.apply {
            addTab(this.newTab().setText("7일"))
            addTab(this.newTab().setText("30일"))
        }

        subscribe(this)
    }

    private fun subscribe(owner: LifecycleOwner) {

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

    private fun lightDarkThemeCheck(): Int {
        return if (requireActivity().resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES) {
            ContextCompat.getColor(mContext, R.color.white)
        } else {
            ContextCompat.getColor(mContext, R.color.black)
        }
    }
}

