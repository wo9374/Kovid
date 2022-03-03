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
import com.project.kovid.model.WeekCovid
import com.project.kovid.util.StringUtil

class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home) {
    //private val viewModel: HomeViewModel by activityViewModels() //activity 의 ViewModel 을 따름
    private val homeViewModel: HomeViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = homeViewModel
        binding.lifecycleOwner = this

        homeViewModel.getWeekCovid()

        subscribe(this)
    }

    private fun subscribe(owner: LifecycleOwner) {
        homeViewModel.weekDecide.observe(owner) {
            //신규 날짜
            binding.txtCurrentDate.text = getString(R.string.current_covid, it[it.lastIndex].day)
            binding.txtCurrentDate.setTextColor(lightDarkThemeCheck())
            //신규 확진자
            binding.txtDecideCnt.text = StringUtil.getDecimalFormatNum(it[it.lastIndex].decideCnt)

            barChartSetting(binding.chart, it)
            barDataSetting(binding.chart, it)
        }
    }

    private fun barDataSetting(chart: BarChart, dataList: ArrayList<WeekCovid>) {
        val entries = ArrayList<BarEntry>()

        dataList.forEachIndexed { index, weekCovid ->
            entries.add(BarEntry(
                (index+1).toFloat(),
                weekCovid.decideCnt.toFloat())
            )
        }

        val set = BarDataSet(entries,"DataSet") //데이터셋 초기화 하기
        set.color = ContextCompat.getColor(requireContext(),R.color.fab_red)

        val dataSet :ArrayList<IBarDataSet> = ArrayList()
        dataSet.add(set)
        val data = BarData(dataSet)
        data.barWidth = 0.3f//막대 너비 설정하기
        chart.run {
            this.data = data //차트의 데이터를 data로 설정해줌.
            setFitBars(true)
            invalidate()
        }
    }

    private fun barChartSetting(chart : BarChart, dataList: ArrayList<WeekCovid>) {
        chart.run {
            description.isEnabled = false // 차트 옆 별도로 표시되는 description

            setMaxVisibleValueCount(dataList.size)    // 최대 표시할 그래프 수
            setPinchZoom(false)           // 핀치줌 설정
            setDrawBarShadow(false)       // 그래프 그림자
            setDrawGridBackground(false)  // 격자 구조 유무

            axisLeft.run { //왼쪽 축, Y 축
                axisMaximum = 300001f  //위치에 선을 그리기 위해 +1f로 맥시멈
                axisMinimum = 0f       //최소값

                granularity = 50000f   //단위마다 선 그리기

                setDrawLabels(true)    //값 적기 허용
                setDrawGridLines(true)    //격자 라인 활용
                setDrawAxisLine(false)    //축 그리기 설정

                axisLineColor = lightDarkThemeCheck() //축 컬러 설정
                gridColor = lightDarkThemeCheck()     //격자 컬러 설정
                textColor = lightDarkThemeCheck()     //라벨 텍스트 컬러
                textSize = 13f      //라벨 텍스트 크기
            }

            xAxis.run {
                position = XAxis.XAxisPosition.BOTTOM  // x축을 아래에 설정
                granularity = 1f      // 1단위 만큼 간격
                setDrawAxisLine(true) // 축 그림
                setDrawGridLines(false) // 격자
                textColor = lightDarkThemeCheck()   //라벨 컬러
                textSize = 12f        // 텍스트 크기
                valueFormatter = MyXAxisFormatter(dataList)
            }

            axisRight.isEnabled = false // 우측 Y축 안보이게
            setTouchEnabled(false)      // 그래프 터치 disable
            animateY(1000)  // 아래서 올라오는 anim
            legend.isEnabled = false    // 차트 범례 설정
        }
    }
    inner class MyXAxisFormatter(private val dataList: ArrayList<WeekCovid>) : ValueFormatter(){
        private val days = arrayOfNulls<String>(dataList.size)

        override fun getAxisLabel(value: Float, axis: AxisBase?): String {
            dataList.forEachIndexed { index, weekCovid ->
                days[index] = weekCovid.day
            }
            return days.getOrNull(value.toInt()-1) ?: value.toString()
        }
    }


    private fun lightDarkThemeCheck() : Int{
        return if (requireActivity().resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES){
            ContextCompat.getColor(mContext, R.color.white)
        }else{
            ContextCompat.getColor(mContext, R.color.black)
        }
    }
}

