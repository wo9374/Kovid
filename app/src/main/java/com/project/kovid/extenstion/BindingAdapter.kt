package com.project.kovid.extenstion

import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.listener.ChartTouchListener
import com.github.mikephil.charting.listener.OnChartGestureListener
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.project.kovid.R
import com.project.kovid.extenstion.customview.CustomChartMarker
import com.project.kovid.model.WeekCovid
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.pow

@BindingAdapter("binding:setNaviVisible")
fun setBottomVisible(bottomNavi: BottomNavigationView, boolean: Boolean){
    if (boolean) bottomNavi.visibility = View.VISIBLE
    else bottomNavi.visibility = View.GONE
}

@BindingAdapter("binding:setImg")
fun setImg(view: ImageView, imgUri: String?){

    if(imgUri != null)
        Glide
            .with(view.context)
            .load(imgUri)
            .override(view.width)    //받아온 이미지 크기 조정
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE) //캐싱된 리소스와 로드할 리소스가 같은 리소스일때 캐싱된 리소스 사용, gif 느릴 경우 사용
            .into(view)
    else
        view.setImageResource(R.drawable.ic_baseline_room_24)
}

/*

   Glide
            .with(view.context)
            .load(imgUri)
            .placeholder()   // 이미지를 보여주는 동안 보여줄 이미지 지정
            .error()         // 에러 났을때 이미지
            .fallback(R.drawable.img_file_no_img)  //load할 url이 null인 경우 등 비어있을 때 보여줄 이미지를 설정
            .asGif() //Gif 로딩시

            //gif 루프 한번돌게
            .listener(new RequestListener<GifDrawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<GifDrawable> target, boolean isFirstResource) {
                    return false;
                }

                @Override
                public boolean onResourceReady(GifDrawable resource, Object model, Target<GifDrawable> target, DataSource dataSource, boolean isFirstResource) {
                    if (resource != null) {
                        resource.setLoopCount(1);
                    }
                    return false;
                }
            })
            .override(view.width)    //받아온 이미지 크기 조정
            .centerCrop()
            .thumbnail(0.1f) //지정한 %비율 만큼 미리 이미지를 가져와서 보여줄 수 있음. ex)0.1f 는 실제 이미지 해상도의 10%로 흐리게 보여줌
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE) //캐싱된 리소스와 로드할 리소스가 같은 리소스일때 캐싱된 리소스 사용, gif 느릴 경우 사용
            .into(view)
}*/

@BindingAdapter(value = ["dataList","uiModeColor"])
fun setChartSetting(chart: BarChart, dataList: ArrayList<WeekCovid>?, uiModeColor : Int){
    chart.setNoDataText(chart.context.getString(R.string.data_loading))  //data 없을때 표시 text

    val cntList = arrayListOf<Int>()    //Bar ChartSet / Max 확진자수 구하기위한 배열
    val entries = ArrayList<BarEntry>() //Bar DataSet  / 그래프 순서, 수치

    dataList?.forEachIndexed { index, weekCovid ->
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
    if (!cntList.isNullOrEmpty()){
        val maxDecide = Collections.max(cntList) //코로나 확진자 최대 값
        var maxDecideLength = maxDecide.toString().length


        if (maxDecide.toString().length > 1){
            maxDecideLength -= 1
            multipli = 10.0.pow(maxDecideLength.toDouble()).toInt() //10의 n승 (0 자릿수 만큼)
        }else multipli = 10

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

        isDragEnabled = true
        isDragDecelerationEnabled = true

        axisRight.isEnabled = false // 우측 Y축 안보이게
        setTouchEnabled(true)      // 그래프 터치 disable / enable
        animateY(1000)  // 아래서 올라오는 anim
        legend.isEnabled = false    // 차트 범례 설정

        axisLeft.run { //왼쪽 축, Y 축
            axisMaximum = maxGraphCount  //끝 위치에 선을 그리기 위해 + 1f로 맥시멈
            axisMinimum = 0f       //최소값
            granularity = (multipli/2).toFloat()   //단위마다 선 그리기

            setDrawLabels(true)      //값 적기 허용
            setDrawGridLines(true)   //격자 라인 활용
            setDrawAxisLine(false)   //축 그리기 설정

            axisLineColor = uiModeColor    //축 컬러 설정
            gridColor = uiModeColor        //격자 컬러 설정
            textColor = uiModeColor        //라벨 텍스트 컬러
            textSize = 13f           //라벨 텍스트 크기
        }

        xAxis.run {
            position = XAxis.XAxisPosition.BOTTOM  // x축을 아래에 설정
            granularity = 1f      // 1단위 만큼 간격
            setDrawAxisLine(true) // 축 그림
            setDrawGridLines(false) // 격자
            textColor = uiModeColor     //라벨 컬러
            textSize = 12f        // 텍스트 크기
            valueFormatter = MyXAxisFormatter(dataList)
        }

        val customMarker = CustomChartMarker(chart.context, R.layout.custom_mpchart_marker)
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

class MyXAxisFormatter(private val dataList: ArrayList<WeekCovid>?) : ValueFormatter() {
    private val days = arrayOfNulls<String>(dataList?.size ?:0)

    override fun getAxisLabel(value: Float, axis: AxisBase?): String {
        dataList?.forEachIndexed { index, weekCovid ->
            days[index] = weekCovid.day
        }
        return days.getOrNull(value.toInt() - 1) ?: value.toString()
    }
}
