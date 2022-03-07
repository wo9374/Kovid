package com.project.kovid.function.home

import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.project.kovid.R
import com.project.kovid.base.BaseFragment
import com.project.kovid.databinding.FragmentHomeBinding
import com.project.kovid.model.WeekCovid

class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home) {
    //private val viewModel: HomeViewModel by activityViewModels() //activity 의 ViewModel 을 따름
    private val homeViewModel: HomeViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = homeViewModel
        binding.lifecycleOwner = this

        homeViewModel.uiModeColor.value = lightDarkThemeCheck()
        homeViewModel.getWeekCovid()

        binding.chart.setNoDataText(getString(R.string.data_loading))  //data 없을때 표시 text
        subscribe(this)
    }

    private fun subscribe(owner: LifecycleOwner) {
        homeViewModel.weekDecide.observe(owner){
            chartDataSet(binding.chart, it)
        }
    }

    /**---------------------------- Bar DataSet  ------------------------------*/
    fun chartDataSet(chart: BarChart, dataList: ArrayList<WeekCovid>){
        val entries = ArrayList<BarEntry>() //Bar DataSet  / 그래프 순서, 수치

        dataList.forEachIndexed { index, weekCovid ->
            val graphIndex = (index + 1).toFloat()         //그래프 순서 1부터 시작
            val graphCnt = weekCovid.decideCnt.toFloat()   //그래프 확진자 수치
            entries.add(BarEntry(graphIndex, graphCnt))
        }

        val set = BarDataSet(entries, "DataSet")     //데이터셋 초기화
        set.color = ContextCompat.getColor(chart.context, R.color.fab_red) //그래프 바 Color

        val dataSet: ArrayList<IBarDataSet> = arrayListOf(set)
        val data = BarData(dataSet)
        data.barWidth = 0.3f  //막대 너비 설정

        chart.run {
            this.data = data  //차트의 데이터를 data로 설정
            setFitBars(true)
            invalidate()
        }
    }

    private fun lightDarkThemeCheck(): Int {
        return if (requireActivity().resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES) {
            ContextCompat.getColor(mContext, R.color.white)
        } else {
            ContextCompat.getColor(mContext, R.color.black)
        }
    }
}

