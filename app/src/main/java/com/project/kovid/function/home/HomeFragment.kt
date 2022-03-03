package com.project.kovid.function.home

import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import com.project.kovid.R
import com.project.kovid.base.BaseFragment
import com.project.kovid.databinding.FragmentHomeBinding
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.project.kovid.extenstion.CustomMPChartMarker
import com.project.kovid.model.WeekCovid
import com.project.kovid.util.StringUtil

class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home) {
    //private val viewModel: HomeViewModel by activityViewModels() //activity 의 ViewModel 을 따름
    private val homeViewModel: HomeViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = homeViewModel
        binding.lifecycleOwner = this

        homeViewModel.color.value = lightDarkThemeCheck()
        homeViewModel.getWeekCovid()

        subscribe(this)
    }

    private fun subscribe(owner: LifecycleOwner) {
        homeViewModel.weekDecide.observe(owner) {
            barChartSetting(binding.chart, it, homeViewModel.color.value!!)
            barDataSetting(binding.chart, it)
        }
    }

    private fun barChartSetting(chart: BarChart, dataList: ArrayList<WeekCovid>, color: Int) {
        chart.run {
            description.isEnabled = false // 차트 옆 별도로 표시되는 description

            setMaxVisibleValueCount(dataList.size)    // 최대 표시할 그래프 수
            setPinchZoom(false)           // 핀치줌 설정
            setDrawBarShadow(false)       // 그래프 그림자
            setDrawGridBackground(false)  // 격자 구조 유무

            axisLeft.run { //왼쪽 축, Y 축
                axisMaximum = 300001f  //끝 위치에 선을 그리기 위해 + 1f로 맥시멈
                axisMinimum = 0f       //최소값

                granularity = 50000f   //단위마다 선 그리기

                setDrawLabels(true)    //값 적기 허용
                setDrawGridLines(true)    //격자 라인 활용
                setDrawAxisLine(false)    //축 그리기 설정

                axisLineColor = color //축 컬러 설정
                gridColor = color     //격자 컬러 설정
                textColor = color     //라벨 텍스트 컬러
                textSize = 13f      //라벨 텍스트 크기
            }

            xAxis.run {
                position = XAxis.XAxisPosition.BOTTOM  // x축을 아래에 설정
                granularity = 1f      // 1단위 만큼 간격
                setDrawAxisLine(true) // 축 그림
                setDrawGridLines(false) // 격자
                textColor = color     //라벨 컬러
                textSize = 12f        // 텍스트 크기
                valueFormatter = MyXAxisFormatter(dataList)
            }

            axisRight.isEnabled = false // 우측 Y축 안보이게
            setTouchEnabled(false)      // 그래프 터치 disable
            animateY(1000)  // 아래서 올라오는 anim
            legend.isEnabled = false    // 차트 범례 설정

            marker = CustomMPChartMarker(mContext, R.layout.custom_mpchart_marker)
        }
    }

    private fun barDataSetting(chart: BarChart, dataList: ArrayList<WeekCovid>) {
        val entries = ArrayList<BarEntry>()

        dataList.forEachIndexed { index, weekCovid ->
            val graphIndex = (index + 1).toFloat()         //그래프 순서 1부터 시작
            val graphCnt = weekCovid.decideCnt.toFloat()   //그래프 확진자 수치
            entries.add(BarEntry(graphIndex, graphCnt))
        }

        val set = BarDataSet(entries, "DataSet")     //데이터셋 초기화
        set.color = ContextCompat.getColor(mContext, R.color.fab_red) //그래프 바 Color

        val dataSet: ArrayList<IBarDataSet> = arrayListOf(set)
        val data = BarData(dataSet)
        data.barWidth = 0.3f  //막대 너비 설정
        chart.run {
            this.data = data  //차트의 데이터를 data로 설정
            setFitBars(true)
            invalidate()
        }
    }

    inner class MyXAxisFormatter(private val dataList: ArrayList<WeekCovid>) : ValueFormatter() {
        private val days = arrayOfNulls<String>(dataList.size)

        override fun getAxisLabel(value: Float, axis: AxisBase?): String {
            dataList.forEachIndexed { index, weekCovid ->
                days[index] = weekCovid.day
            }
            return days.getOrNull(value.toInt() - 1) ?: value.toString()
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

