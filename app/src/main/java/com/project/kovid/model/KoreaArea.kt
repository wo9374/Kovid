package com.project.kovid.model

import com.google.gson.annotations.SerializedName

data class KoreaArea(
    @SerializedName("resultCode") var resultCode : Int,
    @SerializedName("resultMessage") var resultMessage : String,

    //@SerializedName("korea") var korea : AreaData,
    @SerializedName("seoul") var seoul : Area,
    @SerializedName("busan") var busan : Area,
    @SerializedName("daegu") var daegu : Area,
    @SerializedName("incheon") var incheon : Area,
    @SerializedName("gwangju") var gwangju : Area,
    @SerializedName("daejeon") var daejeon : Area,
    @SerializedName("ulsan") var ulsan : Area,
    @SerializedName("sejong") var sejong : Area,
    @SerializedName("gyeonggi") var gyeonggi : Area,
    @SerializedName("gangwon") var gangwon : Area,
    @SerializedName("chungbuk") var chungbuk : Area,
    @SerializedName("chungnam") var chungnam : Area,
    @SerializedName("jeonbuk") var jeonbuk : Area,
    @SerializedName("jeonnam") var jeonnam : Area,
    @SerializedName("gyeongbuk") var gyeongbuk : Area,
    @SerializedName("gyeongnam") var gyeongnam : Area,
    @SerializedName("jeju") var jeju : Area,
    @SerializedName("quarantine") var quarantine : Area, //검역
)

data class Area(
    @SerializedName("countryName") var countryName : String, //시도명(지역명)
    @SerializedName("newCase") var newCase : String,         //신규확진환자수
    @SerializedName("totalCase") var totalCase : String,     //확진환자수
    @SerializedName("recovered") var recovered : String,     //완치자수
    @SerializedName("death") var death : String,             //사망자
    @SerializedName("percentage") var percentage : String,   //발생률
    @SerializedName("newCcase") var newCcase : String,       //전일대비증감-해외유입
    @SerializedName("newFcase") var newFcase : String,       //전일대비증감-지역발생
)