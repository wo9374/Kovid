package com.ljb.domain.entity

data class ChartCovid(
    var createDt: String,           //등록일시분초
    var deathCnt: Int,              //사망자 수
    var decideCnt: Int,             //확진자 수
    var seq: String,                //감염현황 고유값
    var stateDt: String,            //기준일
    var stateTime: String,          //기준시간
    var updateDt: String? = "",     //수정일시분초
)

data class WeekCovid(
    val day : String,
    val decideCnt : Int,
)