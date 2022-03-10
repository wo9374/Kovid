package com.project.kovid.extenstion.customview

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import android.graphics.Canvas
import android.view.LayoutInflater
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.project.kovid.R
import com.project.kovid.databinding.CustomMpchartMarkerBinding
import com.project.kovid.util.StringUtil

@SuppressLint("ViewConstructor")
class CustomChartMarker(context: Context, layoutResource: Int) : MarkerView(context, layoutResource) {

    private var tvContent: TextView = findViewById(R.id.custom_mp_marker)

    // draw override를 사용해 marker의 위치 조정 (bar의 상단 중앙)
    override fun draw(canvas: Canvas) {
        canvas.translate(
            -(width / 2).toFloat(),
            -height.toFloat()
        )
        super.draw(canvas)
    }

    // entry를 content의 텍스트에 지정
    override fun refreshContent(e: Entry, highlight: Highlight?) {
        tvContent.text = StringUtil.getDecimalFormatNum(e.y.toInt())
        super.refreshContent(e, highlight)
    }
}