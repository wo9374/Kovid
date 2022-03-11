package com.project.kovid.model

import com.google.gson.annotations.SerializedName

data class CovidAreaData(
    @SerializedName("resultCode") var resultCode : Int,
    @SerializedName("resultMessage") var resultMessage : String,

    //@SerializedName("korea") var korea : AreaData,
    var areaList : ArrayList<AreaData> = arrayListOf(),

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
    @SerializedName("countryName") var countryName : String,
    @SerializedName("newCase") var newCase : String,
    @SerializedName("totalCase") var totalCase : String,
    @SerializedName("recovered") var recovered : String,
    @SerializedName("death") var death : String,
    @SerializedName("percentage") var percentage : String,
    @SerializedName("newCcase") var newCcase : String,
    @SerializedName("newFcase") var newFcase : String,
)