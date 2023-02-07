package com.ljb.data.remote.model

import com.tickaroo.tikxml.annotation.Element
import com.tickaroo.tikxml.annotation.PropertyElement
import com.tickaroo.tikxml.annotation.Xml

/**
 *  CovidAPI 에서 반환형으로 사용되는 날짜별 일일 확진자 Response
 * */
@Xml(name = "response")
data class ChartData(
    @Element(name = "header")
    val chartHeader: ChartHeader,
    @Element(name = "body")
    val chartBody: ChartBody
)

@Xml(name = "header")
data class ChartHeader(
    @PropertyElement(name = "resultCode")
    val resultCode: Int,
    @PropertyElement(name = "resultMsg")
    val resultMsg: String,
)

@Xml(name = "body")
data class ChartBody(
    @Element(name = "items")
    val chartItems: ChartList,
    @PropertyElement(name = "numOfRows")
    val numOfRows: Int,
    @PropertyElement(name = "pageNo")
    val pageNo: Int,
    @PropertyElement(name = "totalCount")
    val totalCount: Int,
)

@Xml(name = "items")
data class ChartList(
    @Element(name = "item")
    val chartItem: List<ChartResponse>
)

@Xml(name = "item")
data class ChartResponse(
    @PropertyElement(name = "createDt")   //등록일시분초
    var createDt: String,

    @PropertyElement(name = "deathCnt")   //사망자 수
    var deathCnt: Int,

    @PropertyElement(name = "decideCnt")  //확진자 수
    var decideCnt: Int,

    @PropertyElement(name = "seq")        //감염현황 고유값
    var seq: String,

    @PropertyElement(name = "stateDt")    //기준일
    var stateDt: String,

    @PropertyElement(name = "stateTime")  //기준시간
    var stateTime: String,

    @PropertyElement(name = "updateDt")   //수정일시분초
    var updateDt: String? = "",
)