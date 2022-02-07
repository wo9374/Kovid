package com.example.kovid.model

import com.tickaroo.tikxml.annotation.Element
import com.tickaroo.tikxml.annotation.PropertyElement
import com.tickaroo.tikxml.annotation.Xml

@Xml(name = "response")
data class CurrentCovid(
    @Element var covidHeader : CovidHeader,
    @Element var covidBody : CovidBody
)

@Xml(name = "header")
data class CovidHeader(
    @PropertyElement var resultCode : Int,
    @PropertyElement var resultMsg : String
)

@Xml(name = "body")
data class CovidBody(
    @Element var items : List<CovidItem>,
    @PropertyElement var numOfRows : Int,    // 한 페이지 결과 수
    @PropertyElement var pageNo : Int,          // 페이지 수
    @PropertyElement var totalCount : Int,  // 전체 결과 수
)

@Xml
data class CovidItem(
    //var accDefRate : Long,  // 누적확진률
    @PropertyElement(name = "accExamCnt") var accExamCnt : Int?,    // 누적 의심신고 검사자
    @PropertyElement(name = "createDt") var createDt : String?,     // 등록일시분초
    @PropertyElement(name = "deathCnt") var deathCnt : Int?,        // 사망자 수
    @PropertyElement(name = "decideCnt") var decideCnt : Int?,      // 확진자 수
    @PropertyElement(name = "seq") var seq : Int?,                  // 게시글 번호 감염현황 고유값
    @PropertyElement(name = "stateDt") var stateDt : String?,       // 기준일
    @PropertyElement(name = "stateTime") var stateTime : String?,   // 기준시간
    @PropertyElement(name = "updateDt") var updateDt : String?     // 수정일시분초
){
    constructor() : this(null,null,null,null,null,null,null,null)

    override fun toString(): String {
        return "CurrentCovid - \n" +
                //"accDefRate : $accDefRate\n" +
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