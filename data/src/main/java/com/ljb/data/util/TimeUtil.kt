package com.ljb.data.util

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("SimpleDateFormat")
fun Date.getTodayDateString(): String {
    val date = SimpleDateFormat("yyyyMMdd")
    return date.format(this)
}

@SuppressLint("SimpleDateFormat")
fun Date.getPast1Hour(): String {
    val today = this

    // 1시간전
    val cal: Calendar = Calendar.getInstance()
    cal.time = today
    cal.add(Calendar.HOUR, -1)

    val sdFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")  // 포맷변경 ( 년월일 시분초)
    sdFormat.timeZone = TimeZone.getTimeZone("UTC")

    return sdFormat.format(cal.time)
}

@SuppressLint("SimpleDateFormat")
fun Date.getPast1Day(): String {
    val day: Calendar = Calendar.getInstance()
    day.add(Calendar.DATE, -1)

    return SimpleDateFormat("yyyyMMdd").format(day.time)
}

@SuppressLint("SimpleDateFormat")
fun Date.getPast7Day(): String {
    val week: Calendar = Calendar.getInstance()
    week.add(Calendar.DATE, -7)
    return SimpleDateFormat("yyyyMMdd").format(week.time)
}

@SuppressLint("SimpleDateFormat")
fun Date.getPast1MonthCovid(): String {
    val mon: Calendar = Calendar.getInstance()
    mon.add(Calendar.DATE, -31)
    return SimpleDateFormat("yyyyMMdd").format(mon.time)
}

@SuppressLint("SimpleDateFormat")
fun Date.getPast1Month(): String {
    val mon: Calendar = Calendar.getInstance()
    mon.add(Calendar.MONTH, -1)
    return SimpleDateFormat("yyyy-MM-dd").format(mon.time)
}