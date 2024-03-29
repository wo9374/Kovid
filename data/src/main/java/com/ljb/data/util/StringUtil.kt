package com.ljb.data.util

import android.text.Html
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols

fun String.containsMetropolitanCity() : Boolean{
    return this.contains("특별시") || this.contains("광역시")
}
fun String.splitSi() : String {
    var str = this.replace("특별시", "")
    str = str.replace("광역시", "")
    return str
}

fun String.computeStringToInt() = if (this.length == 8) {
    val month = this.substring(4, 6).toInt()
    val day = this.substring(6).toInt()
    "${month}.$day"
} else {
    "compute 오류"
}

fun Int.getDecimalFormatNum() : String {
    val numString = this.toString()
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

fun String.removeHtmlTags(): String =
    Html.fromHtml(this, Html.FROM_HTML_MODE_LEGACY).toString()
