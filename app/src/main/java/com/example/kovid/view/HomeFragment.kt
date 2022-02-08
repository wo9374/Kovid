package com.example.kovid.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.kovid.R
import com.example.kovid.databinding.FragmentHomeBinding
import com.example.kovid.viewmodel.MainViewModel
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet

class HomeFragment : Fragment() {
    lateinit var binding : FragmentHomeBinding
    lateinit var navController : NavController

    //activity 의 ViewModel 을 따름
    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)

        activity?.let {
            binding.viewModel = viewModel
            binding.lifecycleOwner = this
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(binding.root)

        viewModel.getCovidItem()

        //barChartSetting(binding.chart)
        //barDataSetting(binding.chart)
    }



    private fun barDataSetting(chart: BarChart) {
        val entries = ArrayList<BarEntry>()
        entries.add(BarEntry(1.0f,20.0f))
        entries.add(BarEntry(2.0f,70.0f))
        entries.add(BarEntry(3.0f,30.0f))
        entries.add(BarEntry(4.0f,90.0f))
        entries.add(BarEntry(5.0f,70.0f))
        entries.add(BarEntry(6.0f,30.0f))
        entries.add(BarEntry(7.0f,90.0f))



        var set = BarDataSet(entries,"DataSet")//데이터셋 초기화 하기
        set.color = ContextCompat.getColor(requireContext(),R.color.fab_green)

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

    private fun barChartSetting(chart : BarChart) {
        chart.run {
            description.isEnabled = false // 차트 옆 별도로 표시되는 description

            setMaxVisibleValueCount(7)    // 최대 표시할 그래프 수
            setPinchZoom(false)           // 핀치줌 설정
            setDrawBarShadow(false)       // 그래프 그림자
            setDrawGridBackground(false)  // 격자 구조 유무

            axisLeft.run {     //왼쪽 축, Y 축
                axisMaximum = 101f  //100 위치에 선을 그리기 위해 101f로 맥시멈
                axisMinimum = 0f    //최소값
                granularity = 50f   //50 단위마다 선 그리기
                setDrawLabels(true) //값 적기 허용
                setDrawGridLines(true)    //격자 라인 활용
                setDrawAxisLine(false)    //축 그리기 설정
                axisLineColor = ContextCompat.getColor(context, R.color.black) //축 컬러 설정
                gridColor = ContextCompat.getColor(context, R.color.black)     //격자 컬러 설정
                textColor = ContextCompat.getColor(context, R.color.black)     //라벨 텍스트 컬러
                textSize = 13f      //라벨 텍스트 크기
            }

            xAxis.run {
                position = XAxis.XAxisPosition.BOTTOM  // x축을 아래에 설정
                granularity = 1f      // 1단위 만큼 간격
                setDrawAxisLine(true) // 축 그림
                setDrawGridLines(false) // 격자
                textColor = ContextCompat.getColor(context, R.color.fab_red)   //라벨 컬러
                textSize = 12f        // 텍스트 크기
                valueFormatter = MyXAxisFormatter()
            }

            axisRight.isEnabled = false // 우측 Y축 안보이게
            setTouchEnabled(false)      // 그래프 터치 disable
            animateY(1000)  // 아래서 올라오는 anim
            legend.isEnabled = false    // 차트 범례 설정
        }
    }

    inner class MyXAxisFormatter : ValueFormatter(){
        private val days = arrayOf("1차","2차","3차","4차","5차","6차","7차")
        override fun getAxisLabel(value: Float, axis: AxisBase?): String {
            return days.getOrNull(value.toInt()-1) ?: value.toString()
        }
    }
}