package com.project.kovid.model

import com.google.gson.annotations.SerializedName

class CovidCountData {
    @SerializedName("resultCode")
    private val resultCode: String = ""

    @SerializedName("resultMsg")
    private val resultMsg: String = ""

    @SerializedName("createDt")
    private val createDt: String = ""        //등록일시분초

    @SerializedName("deathCnt")
    private val deathCnt: String = ""        //사망자 수

    @SerializedName("decideCnt")
    private val decideCnt: String = ""       //확진자 수

    @SerializedName("seq")                //감염현황 고유값
    private val seq: String = ""

    @SerializedName("stateDt")            //기준일
    private val stateDt: String = ""

    @SerializedName("stateTime")          //기준시간
    private val stateTime: String = ""

    @SerializedName("updateDt")           //수정일시분초
    private val updateDt: String? = null
}