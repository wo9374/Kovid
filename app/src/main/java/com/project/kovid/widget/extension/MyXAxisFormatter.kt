package com.project.kovid.widget.extension

import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.formatter.ValueFormatter
import com.ljb.domain.entity.WeekCovid

class MyXAxisFormatter(private val dataList: List<WeekCovid>?) : ValueFormatter() {
    private val days = arrayOfNulls<String>(dataList?.size ?:0)

    override fun getAxisLabel(value: Float, axis: AxisBase?): String {
        dataList?.forEachIndexed { index, weekCovid ->
            days[index] = weekCovid.day
        }
        return days.getOrNull(value.toInt() - 1) ?: value.toString()
    }
}