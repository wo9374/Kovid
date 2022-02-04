package com.example.kovid.model

import com.google.gson.annotations.SerializedName

data class CurrentCovidBody(
    @SerializedName("numOfRows") var numOfRows : Int,    // 한 페이지 결과 수
    @SerializedName("pageNo") var pageNo : Int,          // 페이지 수
    @SerializedName("totalCount") var totalCount : Int,  // 전체 결과 수
){
    override fun toString(): String {
        return "CurrentCovidBody - \n" +
                "numOfRows : $numOfRows\n" +
                "pageNo : $pageNo\n" +
                "totalCount : $totalCount\n"
    }
}

data class CurrentCovid(
    @SerializedName("numOfRows") var numOfRows : Int,    // 한 페이지 결과 수
    @SerializedName("pageNo") var pageNo : Int,          // 페이지 수
    @SerializedName("totalCount") var totalCount : Int,  // 전체 결과 수

    @SerializedName("accDefRate") var accDefRate : Long,  // 누적확진률
    @SerializedName("accExamCnt")var accExamCnt : Int,    // 누적 의심신고 검사자
    @SerializedName("createDt")var createDt : String,     // 등록일시분초
    @SerializedName("deathCnt")var deathCnt : Int,        // 사망자 수
    @SerializedName("decideCnt")var decideCnt : Int,      // 확진자 수
    @SerializedName("seq")var seq : Int,                  // 게시글 번호 감염현황 고유값
    @SerializedName("stateDt")var stateDt : String,       // 기준일
    @SerializedName("stateTime")var stateTime : String,   // 기준시간
    @SerializedName("updateDt")var updateDt : String?     // 수정일시분초
){
    override fun toString(): String {
        return "CurrentCovid - \n" +
                "numOfRows : $numOfRows\n" +
                "pageNo : $pageNo\n" +
                "totalCount : $totalCount\n\n" +

                "accDefRate : $accDefRate\n" +
                "accExamCnt : $accExamCnt\n" +
                "createDt : $createDt\n" +
                "deathCnt : $deathCnt\n" +
                "decideCnt : $decideCnt\n" +
                "seq : $seq\n" +
                "stateDt : $stateDt\n" +
                "stateTime : $stateTime\n" +
                "updateDt : $updateDt\n]"
    }
}