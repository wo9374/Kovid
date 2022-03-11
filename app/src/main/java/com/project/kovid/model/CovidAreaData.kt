package com.project.kovid.model

import com.google.gson.annotations.SerializedName

data class CovidAreaData(
    @SerializedName("resultCode") var resultCode : Int,
    @SerializedName("resultMessage") var resultMessage : String,

    //@SerializedName("korea") var korea : AreaData,
    @SerializedName("seoul") var seoul : AreaData,
    @SerializedName("busan") var busan : AreaData,
    @SerializedName("daegu") var daegu : AreaData,
    @SerializedName("incheon") var incheon : AreaData,
    @SerializedName("gwangju") var gwangju : AreaData,
    @SerializedName("daejeon") var daejeon : AreaData,
    @SerializedName("ulsan") var ulsan : AreaData,
    @SerializedName("sejong") var sejong : AreaData,
    @SerializedName("gyeonggi") var gyeonggi : AreaData,
    @SerializedName("gangwon") var gangwon : AreaData,
    @SerializedName("chungbuk") var chungbuk : AreaData,
    @SerializedName("chungnam") var chungnam : AreaData,
    @SerializedName("jeonbuk") var jeonbuk : AreaData,
    @SerializedName("jeonnam") var jeonnam : AreaData,
    @SerializedName("gyeongbuk") var gyeongbuk : AreaData,
    @SerializedName("gyeongnam") var gyeongnam : AreaData,
    @SerializedName("jeju") var jeju : AreaData,
    @SerializedName("quarantine") var quarantine : AreaData, //검역
)

data class AreaData(
    @SerializedName("countryName") var countryName : String, //시도명(지역명)
    @SerializedName("newCase") var newCase : String,         //신규확진환자수
    @SerializedName("totalCase") var totalCase : String,     //확진환자수
    @SerializedName("recovered") var recovered : String,     //완치자수
    @SerializedName("death") var death : String,             //사망자
    @SerializedName("percentage") var percentage : String,   //발생률
    @SerializedName("newCcase") var newCcase : String,       //전일대비증감-해외유입
    @SerializedName("newFcase") var newFcase : String,       //전일대비증감-지역발생
)