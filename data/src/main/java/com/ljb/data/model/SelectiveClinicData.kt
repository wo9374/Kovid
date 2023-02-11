package com.ljb.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.android.gms.maps.model.LatLng
import com.google.gson.annotations.SerializedName
import com.google.maps.android.clustering.ClusterItem
import com.tickaroo.tikxml.annotation.Element
import com.tickaroo.tikxml.annotation.PropertyElement
import com.tickaroo.tikxml.annotation.Xml


/**
 * 선별진료소 JSON 포멧 Response
 * */
data class SelectiveClinicJsonResponse(
    @SerializedName("resultCode") val resultCode: Int,                  //결과코드
    @SerializedName("resultMsg") val resultMsg: String,                 //결과 메시지

    @SerializedName("numOfRows") val numOfRows: Int,                    //한 페이지 결과 수
    @SerializedName("pageNo") val pageNo: Int,                          //페이지 번호
    @SerializedName("totalCount") val totalCount: Int,                  //전체 결과 수

    @SerializedName("items") val items: List<SelectiveClinicJson>
)


@Entity(tableName = "selective_clinic")
data class SelectiveClinicJson(
    @SerializedName("addr") val addr: String,                           //주소                //경기도 하남시 신장2동 520
    @SerializedName("clinicName") val clinicName: String,               //선별 진료소명        //하남시보건소
    @SerializedName("create_dt") val create_dt: String?,                   //등록일자             //2022-04-06
    @SerializedName("disabledFacility") val disabledFacility: String,   //장애인시설           //""
    @SerializedName("gubun") val gubun: String?,                         //진료소 구분          //null
    @SerializedName("satdayOperTime") val satdayOperTime: String,       //토요일 운영시간       //09:00~17:00
    @SerializedName("satdayYn") val satdayYn: String,                   //토요일 운영여부       //Y
    @SerializedName("sido") val sido: String,                           //시도명               //경기
    @SerializedName("sigungu") val sigungu: String,                     //시군구명             //하남시
    @SerializedName("smpcolYn") val smpcolYn: String,                   //검체 채취 가능여부    //Y
    @SerializedName("telNo") val telNo: String,                         //의료기관 연락처       //031-790-5917
    @SerializedName("weekOperTime") val weekOperTime: String,           //주중 운영시간         //09:00~17:00
    @SerializedName("weekYn") val weekYn: String,                       //주중 운영여부         //Y
    @SerializedName("weekendOperTime") val weekendOperTime: String,     //주말,공휴일 운영시간   //09:00~17:00
    @SerializedName("weekendYn") val weekendYn: String,                 //주말 공휴일 운영여부   //Y

    @PrimaryKey(autoGenerate = true)
    val primaryKey: Int = 0,
)

data class SelectiveCluster(
    val addr: String,
    val sido: String,
    val sigungu: String,
    val clinicName: String,
    val telNo: String,
    val weekYn: String,
    val weekOperTime: String,
    val satdayYn: String,
    val satdayOperTime: String,
    val weekendYn: String,
    val weekendOperTime: String,

    val lat : Double = 0.0,
    val lng: Double = 0.0,
): ClusterItem{
    override fun getPosition(): LatLng = LatLng(lat, lng)
    override fun getTitle(): String = clinicName
    override fun getSnippet(): String = "$addr\n$telNo"
}

/**
 * 선별진료소 XML 포멧 Response
 * */
/*@Xml(name = "header")
data class XmlHeader(
    @PropertyElement(name = "resultCode") val resultCode: Int,
    @PropertyElement(name = "resultMsg") val resultMsg: String,
)

@Xml(name = "response")
data class SelectiveClinicResponse(
    @Element(name = "header") val header: XmlHeader,
    @Element(name = "body") val body: SelectiveClinicBody,
)

@Xml(name = "body")
data class SelectiveClinicBody(
    @Element(name = "items") val hospitals: SelectiveClinicList,
    @PropertyElement(name = "numOfRows") val numOfRows: Int,
    @PropertyElement(name = "pageNo") val pageNo: Int,
    @PropertyElement(name = "totalCount") val totalCount: Int,
)

@Xml(name = "items")
data class SelectiveClinicList(
    @Element(name = "item")
    val clinicList: List<SelectiveClinicXml>
)

@Xml(name = "item")
data class SelectiveClinicXml(
    @PropertyElement(name = "addr")val addr: String,                           //주소                //경기도 하남시 신장2동 520
    @PropertyElement(name = "clinicName")val clinicName: String,               //선별 진료소명        //하남시보건소
    @PropertyElement(name = "crate_dt")val crate_dt: String,                   //등록일자             //2022-04-06
    @PropertyElement(name = "disabledFacility")val disabledFacility: String,   //장애인시설           //""
    @PropertyElement(name = "gubun")val gubun: String,                         //진료소 구분          //null
    @PropertyElement(name = "satdayOperTime")val satdayOperTime: String,       //토요일 운영시간       //09:00~17:00
    @PropertyElement(name = "satdayYn")val satdayYn: String,                   //토요일 운영여부       //Y
    @PropertyElement(name = "sido")val sido: String,                           //시도명               //경기
    @PropertyElement(name = "sigungu")val sigungu: String,                     //시군구명             //하남시
    @PropertyElement(name = "smpcolYn")val smpcolYn: String,                   //검체 채취 가능여부    //Y
    @PropertyElement(name = "telNo")val telNo: String,                         //의료기관 연락처       //031-790-5917
    @PropertyElement(name = "weekOperTime")val weekOperTime: String,           //주중 운영시간         //09:00~17:00
    @PropertyElement(name = "weekYn")val weekYn: String,                       //주중 운영여부         //Y
    @PropertyElement(name = "weekendOperTime")val weekendOperTime: String,     //주말,공휴일 운영시간   //09:00~17:00
    @PropertyElement(name = "weekendYn")val weekendYn: String,                 //주말 공휴일 운영여부   //Y
)*/