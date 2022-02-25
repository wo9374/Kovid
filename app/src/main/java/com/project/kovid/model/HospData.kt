package com.project.kovid.model

import com.google.android.gms.maps.model.LatLng
import com.tickaroo.tikxml.annotation.Element
import com.tickaroo.tikxml.annotation.PropertyElement
import com.tickaroo.tikxml.annotation.Xml

/**병원정보 DataClass*/
data class HospPlace(
    var address: String,
    var yadmNm: String,
    var latLng : LatLng,
    var telno: String,
)

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
    @PropertyElement(name = "adtFrDd")
    var adtFrDd: String?,

    @PropertyElement(name = "sgguNm")
    var sgguNm: String?,

    @PropertyElement(name = "sidoNm")
    var sidoNm: String?,

    @PropertyElement(name = "spclAdmTyCd")
    var spclAdmTyCd: String?,

    @PropertyElement(name = "telno")
    var telno: String?,

    @PropertyElement(name = "yadmNm")
    var yadmNm: String?
)







