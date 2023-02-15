package com.ljb.data.model

import com.google.gson.annotations.SerializedName

/**
* 데이터 찾는 지역 바운더리를 얻어오기 위한 DataClass
* */
data class PolygonOsmId(
    @SerializedName("addr")val place_id : Int,
    //@SerializedName("licence")val licence : String,
    @SerializedName("osm_type")val osm_type : String,
    @SerializedName("osm_id")val osm_id : Int,
    @SerializedName("boundingbox")val boundingbox : List<String>,
    @SerializedName("lat")val lat: String,
    @SerializedName("lon")val lon: String,
    @SerializedName("display_name")val display_name: String,
    //@SerializedName("place_rank")val place_rank: Int,
    @SerializedName("category")val category: String,
    @SerializedName("type")val type: String,
    @SerializedName("importance")val importance: Double,
    //@SerializedName("icon")val icon: String,
)

data class PolyGonResponse(
    @SerializedName("centroid") val centroid: PolygonCenter,
    @SerializedName("geometry") val geometry: PolygonGeoJson,
)

data class PolygonCenter(
    @SerializedName("type") val type: String,
    @SerializedName("coordinates") val centerLatLng: List<Double>
)

data class PolygonGeoJson(
    @SerializedName("type") val type: String,
    @SerializedName("coordinates") val polygonLatLng: List<List<List<Double>>>
)
