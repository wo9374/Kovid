package com.ljb.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem
import com.tickaroo.tikxml.annotation.Element
import com.tickaroo.tikxml.annotation.PropertyElement
import com.tickaroo.tikxml.annotation.Xml

/**
* 선별 진료소 API JSON 으로 요청해도 response XML 으로 와서 임시 XML 파싱
* */

@Xml(name = "header")
data class ClinicHeader(
    @PropertyElement(name = "resultCode") val resultCode: Int,
    @PropertyElement(name = "resultMsg") val resultMsg: String,
)

@Xml(name = "response")
data class ClinicResponse(
    @Element(name = "header") val header: ClinicHeader,
    @Element(name = "body") val body: HospitalBody,
)

@Xml(name = "body")
data class HospitalBody(
    @Element(name = "items") val hospitals: SelectiveList,
    @PropertyElement(name = "numOfRows") val numOfRows: Int,
    @PropertyElement(name = "pageNo") val pageNo: Int,
    @PropertyElement(name = "totalCount") val totalCount: Int,
)

@Xml(name = "items")
data class SelectiveList(
    @Element(name = "item")
    val clinicList: List<SelectiveResponse>
)

@Xml(name = "item")
data class SelectiveResponse(
    @PropertyElement(name = "addr")val addr: String,                           //주소                //경기도 하남시 신장2동 520
    @PropertyElement(name = "clinicName")val clinicName: String,               //선별 진료소명        //하남시보건소
    @PropertyElement(name = "createDt")val crate_dt: String,                   //등록일자             //2022-04-06
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
)

@Entity(tableName = "selective_clinic")
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

    @PrimaryKey(autoGenerate = false) val latLng : Pair<Double, Double>
) : ClusterItem{
    override fun getPosition(): LatLng = LatLng(latLng.first, latLng.second)
    override fun getTitle(): String = clinicName
    override fun getSnippet(): String = "$addr\n$telNo"
}

//선별 진료소 API JSON 으로 요청해도 response XML 으로 와서 임시 주석
/*
data class HospitaData(
    @SerializedName("resultCode") val resultCode: Int,                  //결과코드
    @SerializedName("resultMsg") val resultMsg: String,                 //결과 메시지

    @SerializedName("numOfRows") val numOfRows: Int,                    //한 페이지 결과 수
    @SerializedName("pageNo") val pageNo: Int,                          //페이지 번호
    @SerializedName("totalCount") val totalCount: Int,                  //전체 결과 수

    @SerializedName("items") val items: List<HospitalResponse>
)

data class HospitalResponse(
    @SerializedName("addr") val addr: String,                           //주소                //경기도 하남시 신장2동 520
    @SerializedName("clinicName") val clinicName: String,               //선별 진료소명        //하남시보건소
    @SerializedName("crate_dt") val crate_dt: String,                   //등록일자             //2022-04-06
    @SerializedName("disabledFacility") val disabledFacility: String,   //장애인시설           //""
    //@SerializedName("gubun") val gubun: String,                         //진료소 구분          //null
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
)
*/