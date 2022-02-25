package com.project.kovid.model

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem
import com.tickaroo.tikxml.annotation.Element
import com.tickaroo.tikxml.annotation.PropertyElement
import com.tickaroo.tikxml.annotation.Xml

/**
 * 코로나19병원정보 API DataClass
 * */
@Xml(name = "response")
data class HospData(
    @Element(name = "header")
    val header: HospHeader,
    @Element(name = "body")
    val body: HospBody
)

@Xml(name = "header")
data class HospHeader(
    @PropertyElement(name = "resultCode")
    val resultCode: Int,
    @PropertyElement(name = "resultMsg")
    val resultMsg: String,
)

@Xml(name = "body")
data class HospBody(
    @Element(name = "items")
    val items: HospItems,
    @PropertyElement(name = "numOfRows")
    val numOfRows: Int,
    @PropertyElement(name = "pageNo")
    val pageNo: Int,
    @PropertyElement(name = "totalCount")
    val totalCount: Int,
)

@Xml(name = "items")
data class HospItems(
    @Element(name = "item")
    val item: List<HospItem>
)

@Xml(name = "item")
data class HospItem(
    @PropertyElement(name = "addr")      //주소
    var addr: String,

//    @PropertyElement(name = "mgtStaDd")  //운영시작일자
//    var mgtStaDd: String?,

    @PropertyElement(name = "pcrPsblYn") //PCR가능여부
    var pcrPsblYn: String?,

    @PropertyElement(name = "ratPsblYn") //RAT(신속항원검사)가능여부
    var ratPsblYn: String?,

    @PropertyElement(name = "recuClCd")  //11:종합병원, 21:병원, 31:의원
    var recuClCd: String?,

//    @PropertyElement(name = "rnum")      //판명불가
//    var rnum: String?,

//    @PropertyElement(name = "rprtWorpClicFndtTgtYn") //호흡기전담클리닉 여부
//    var rprtWorpClicFndtTgtYn: String?,

    @PropertyElement(name = "sgguCdNm")  //강동구
    var sgguCdNm: String?,

    @PropertyElement(name = "sidoCdNm")  //경기도
    var sidoCdNm: String?,

    @PropertyElement(name = "telno")     //요양기관전화번호
    var telno: String?,

    @PropertyElement(name = "XPosWgs84") //경도
    var XPosWgs84: Double,

    @PropertyElement(name = "YPosWgs84") //위도
    var YPosWgs84: Double,

    @PropertyElement(name = "yadmNm")    //요양기관명
    var yadmNm: String,

    @PropertyElement(name = "ykihoEnc")  //암호화된 요양기호
    var ykihoEnc: String,

) : ClusterItem {
    override fun getPosition(): LatLng {
        return LatLng(YPosWgs84,XPosWgs84)
    }

    override fun getTitle(): String {
        return yadmNm
    }

    override fun getSnippet(): String {
        return addr
    }
}







