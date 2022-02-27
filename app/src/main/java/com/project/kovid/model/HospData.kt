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
    @PropertyElement(name = "adtFrDd") //운영가능일자
    var adtFrDd: String,

    @PropertyElement(name = "sgguNm")  //시군구명
    var sgguNm: String,

    @PropertyElement(name = "sidoNm")  //시도명
    var sidoNm: String,

    @PropertyElement(name = "spclAdmTyCd") //A0: 국민안심병원 /97: 코로나검사 실시기관(무증상) /99: 코로나 선별진료소 운영기관 (의심증상)
    var spclAdmTyCd: String,

    @PropertyElement(name = "telno")   //전화번호
    var telno: String,

    @PropertyElement(name = "yadmNm")  //기관명
    var yadmNm: String,

)

data class HospMarker(
    val latLng: LatLng,
    var spclAdmTyCd: String,

    var sidoNm: String,
    var sgguNm: String,
    val yadmNm: String,

    var telno: String,
) : ClusterItem {
    override fun getPosition(): LatLng {
        return latLng
    }

    override fun getTitle(): String {
        return yadmNm
    }

    override fun getSnippet(): String {
        return "$sidoNm $sgguNm\n$yadmNm"
    }
}







