package com.project.kovid.widget.util

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols

object StringUtil {
    fun computeStringToInt(string:String) : String{
        if (string.length == 8){
            val month = string.substring(4,6).toInt()
            val day = string.substring(6).toInt()
            return "${month}.$day"
        }
        return "compute 오류"
    }

    fun getDecimalFormatNum(num: Int): String {

        val numString = num.toString()
        var result = "0"
        if (numString.isEmpty()) return result

        val dfs = DecimalFormatSymbols()
        dfs.groupingSeparator = ','

        val df = DecimalFormat("#,###")  //#대신 0을 주면 무조건 0으로 대체
        df.groupingSize = 3              //3자리수 마다 그룹핑
        df.decimalFormatSymbols = dfs

        val dPrice = numString.toDouble()
        result = df.format(dPrice)

        return result
    }
}