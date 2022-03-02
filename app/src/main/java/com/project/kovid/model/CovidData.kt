package com.project.kovid.model

import com.tickaroo.tikxml.annotation.Element
import com.tickaroo.tikxml.annotation.PropertyElement
import com.tickaroo.tikxml.annotation.Xml

/**
 * 코로나 감염정보 API DataClass
 * */
@Xml(name = "response")
data class CovidData(
    @Element(name = "header")
    val header: CovidHeader,
    @Element(name = "body")
    val body: CovidBody
)

@Xml(name = "header")
data class CovidHeader(
    @PropertyElement(name = "resultCode")
    val resultCode: Int,
    @PropertyElement(name = "resultMsg")
    val resultMsg: String,
)

@Xml(name = "body")
data class CovidBody(
    @Element(name = "items")
    val items: CovidItems,
    @PropertyElement(name = "numOfRows")
    val numOfRows: Int,
    @PropertyElement(name = "pageNo")
    val pageNo: Int,
    @PropertyElement(name = "totalCount")
    val totalCount: Int,
)

@Xml(name = "items")
data class CovidItems(
    @Element(name = "item")
    val item: List<CovidItem>
)

@Xml(name = "item")
data class CovidItem(
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
    var updateDt: String?,

    )