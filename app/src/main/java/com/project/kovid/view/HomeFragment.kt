package com.project.kovid.view

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.google.android.material.tabs.TabLayout
import com.ljb.data.util.getDecimalFormatNum
import com.ljb.domain.UiState
import com.ljb.domain.entity.WeekCovid
import com.project.kovid.R
import com.project.kovid.base.BaseFragment
import com.project.kovid.databinding.FragmentHomeBinding
import com.project.kovid.viewmodel.HomeViewModel
import com.project.kovid.widget.AreaListAdapter
import com.project.kovid.widget.extension.MyXAxisFormatter
import com.project.kovid.widget.extension.customview.CustomChartMarker
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import java.util.*
import kotlin.math.pow

/**
 * Fragment 생성시 HomeViewModel 생성되어, api 결과를 flow로 데이터 발생시 수집
 * */
@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home) {
    private val chartViewModel: HomeViewModel by viewModels()

    private val listAdapter = AreaListAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            lifecycleOwner = viewLifecycleOwner

            chart.apply {
                setOnChartValueSelectedListener(valueOnSelectedListener)
                setOnTouchListener(chartOnTouchListener)
            }
            chartTabLayout.addOnTabSelectedListener(tabOnTabSelectedListener)
            areaRecycler.apply {
                adapter = listAdapter
                addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL)) //구분선
            }
        }

        lifecycleScope.launch {

            launch {
                chartViewModel.covidList
                    .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                    .collectLatest { uiState ->
                        when (uiState) {
                            is UiState.Loading -> {
                                binding.apply {
                                    txtCurrentDate.text = getString(R.string.data_loading)
                                    txtDecideCnt.text = getString(R.string.default_decide)
                                    chart.setNoDataText(getString(R.string.data_loading))
                                }
                            }
                            is UiState.Complete -> {    //받아온 확진자 데이터 Collect 시작
                                setChartSetting(binding.chart, uiState.data)
                            }
                            is UiState.Fail -> {
                                binding.apply {
                                    txtCurrentDate.text = getString(R.string.network_error)
                                    chart.setNoDataText(getString(R.string.network_error))
                                }
                            }
                        }
                    }
            }

            launch {
                chartViewModel.areaList
                    .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                    .distinctUntilChanged()
                    .collectLatest {uiState ->
                        when (uiState) {
                            is UiState.Loading -> {
                                binding.areaErrorText.text = getString(R.string.data_loading)
                            }
                            is UiState.Complete -> {
                                binding.areaErrorText.visibility = View.GONE
                                listAdapter.submitList(uiState.data)
                            }
                            is UiState.Fail -> {
                                binding.apply {
                                    areaErrorText.visibility = View.VISIBLE
                                    areaErrorText.text = uiState.message
                                }
                            }
                        }

                    }
            }
        }
    }

    private fun setChartSetting(chart: BarChart, dataList: List<WeekCovid>) {
        binding.apply {
            val todayCovid = dataList[dataList.lastIndex]
            txtCurrentDate.text = getString(R.string.current_covid, todayCovid.day)
            txtDecideCnt.text = todayCovid.decideCnt.getDecimalFormatNum()
        }

        val cntList = arrayListOf<Int>()    //Bar ChartSet / Max 확진자수 구하기위한 배열
        val entries = ArrayList<BarEntry>() //Bar DataSet  / 그래프 순서, 수치

        dataList.forEachIndexed { index, weekCovid ->
            cntList.add(weekCovid.decideCnt)

            val graphIndex = (index + 1).toFloat()         //그래프 순서 1부터 시작
            val graphCnt = weekCovid.decideCnt.toFloat()   //그래프 확진자 수치

            val entry = BarEntry(graphIndex, graphCnt)
            entry.data = dataList[index]
            entries.add(entry)
        }

        var multipli = 0
        var maxGraphCount = 0.0f

        /**---------------------------- 일일 확진자 Text  ------------------------------*/
        if (!cntList.isNullOrEmpty()) {
            val maxDecide = Collections.max(cntList) //코로나 확진자 최대 값
            var maxDecideLength = maxDecide.toString().length


            if (maxDecide.toString().length > 1) {
                maxDecideLength -= 1
                multipli = 10.0.pow(maxDecideLength.toDouble()).toInt() //10의 n승 (0 자릿수 만큼)
            } else multipli = 10

            val num = (maxDecide + multipli) / multipli
            maxGraphCount = (num * multipli).toFloat() + 1f
        }

        /**---------------------------- Bar ChartSetting  ------------------------------*/
        chart.run {
            description.isEnabled = false   // 차트 옆 별도로 표시되는 description
            setMaxVisibleValueCount(dataList?.size ?: 0)   // 최대 표시할 그래프 수
            //setPinchZoom(false)           // 핀치줌 설정 (기능 안됨)
            setScaleEnabled(false)          // 모든 확대/축소 비활성화
            setDrawBarShadow(false)         // 그래프 그림자
            setDrawGridBackground(false)    // 격자 구조 유무

            isDragEnabled = true            // 차트 드래그 유무
            isDragDecelerationEnabled = true

            axisRight.isEnabled = false     // 우측 Y축 안보이게
            setTouchEnabled(true)           // 그래프 터치 disable / enable
            animateY(1000)      // 아래서 올라오는 anim
            legend.isEnabled = false        // 차트 범례 설정

            axisLeft.run { //왼쪽 축, Y 축
                axisMaximum = maxGraphCount  //끝 위치에 선을 그리기 위해 + 1f로 맥시멈
                axisMinimum = 0f             //최소값
                granularity = (multipli / 2).toFloat()   //단위마다 선 그리기

                setDrawLabels(true)      //값 적기 허용
                setDrawGridLines(true)   //격자 라인 활용
                setDrawAxisLine(false)   //축 그리기 설정

                ContextCompat.getColor(context, R.color.day_night_color).run {
                    axisLineColor = this //축 컬러 설정
                    gridColor = this     //격자 컬러 설정
                    textColor = this     //라벨 텍스트 컬러
                }
                textSize = 13f           //라벨 텍스트 크기
            }

            xAxis.run {
                position = XAxis.XAxisPosition.BOTTOM  // x축을 아래에 설정
                granularity = 1f            // 1단위 만큼 간격
                setDrawAxisLine(true)       // 축 그림
                setDrawGridLines(false)     // 격자
                textColor = ContextCompat.getColor(context, R.color.day_night_color) //라벨 컬러
                textSize = 12f              // 텍스트 크기
                valueFormatter = MyXAxisFormatter(dataList)
            }

            val customMarker = CustomChartMarker(context, R.layout.custom_mpchart_marker)
            marker = customMarker
        }

        /**---------------------------- Bar DataSet  ------------------------------*/
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

    private val valueOnSelectedListener = object : OnChartValueSelectedListener {
        override fun onValueSelected(e: Entry?, h: Highlight?) {
            e.let {
                val weekCovid = e?.data as WeekCovid
                binding.txtCurrentDate.text = getString(R.string.current_covid, weekCovid.day)
                binding.txtDecideCnt.text = weekCovid.decideCnt.getDecimalFormatNum()
                Log.d("타입", weekCovid.day)
            }
        }

        override fun onNothingSelected() {}
    }

    private val tabOnTabSelectedListener = object : TabLayout.OnTabSelectedListener {
        override fun onTabSelected(tab: TabLayout.Tab?) {
            Log.d("탭온 클릭", "${tab?.position}")
            when (tab?.position) {
                0 -> chartViewModel.weekDataSet()
                1 -> chartViewModel.monthDataSet()
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

