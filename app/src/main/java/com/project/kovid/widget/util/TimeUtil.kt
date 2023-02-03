package com.project.kovid.widget.util

import java.text.SimpleDateFormat
import java.util.*

object TimeUtil {

    fun getTodayDate() : String{
        val today = Date()
        val date = SimpleDateFormat("yyyyMMdd")
        val toDay: String = date.format(today)

        return toDay
    }

    fun getPast1Hour(): String{
        val today = Date()
        val date = SimpleDateFormat("yyyy-MM-dd")

        // 1시간전
        val cal: Calendar = Calendar.getInstance()
        cal.time = today
        cal.add(Calendar.HOUR, -1)

        val sdformat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")  // 포맷변경 ( 년월일 시분초)
        sdformat.timeZone = TimeZone.getTimeZone("UTC")

        val beforeHour: String = sdformat.format(cal.time)

        return beforeHour
    }

    fun getPast1Day():String{
        val day: Calendar = Calendar.getInstance()
        day.add(Calendar.DATE, -1)
        val beforeDate = SimpleDateFormat("yyyyMMdd").format(day.time)

        return beforeDate
    }

    fun getPast7Day():String{
        val week: Calendar = Calendar.getInstance()
        week.add(Calendar.DATE, -7)
        val beforeWeek = SimpleDateFormat("yyyyMMdd").format(week.time)
        return beforeWeek
    }

    fun getPast1MonthCovid():String{
        val mon: Calendar = Calendar.getInstance()
        mon.add(Calendar.DATE, -31)
        val beforeMonth = SimpleDateFormat("yyyyMMdd").format(mon.time)
        return beforeMonth
    }

    fun getPast1Month(): String{
        val mon: Calendar = Calendar.getInstance()
        mon.add(Calendar.MONTH, -1)
        val beforeMonth = SimpleDateFormat("yyyy-MM-dd").format(mon.time)
        return beforeMonth
    }
}