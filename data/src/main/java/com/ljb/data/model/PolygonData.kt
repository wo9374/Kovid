package com.ljb.data.model

import com.google.android.gms.maps.model.LatLng
import com.google.gson.annotations.SerializedName

/*import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable*/

/**
* 데이터 찾는 지역 바운더리를 얻어오기 위한 DataClass
* */
data class PolygonOsmId(
    @SerializedName("addr")val place_id : Int,
    //@SerializedName("licence")val licence : String,
    @SerializedName("osm_type")val osm_type : String,
    @SerializedName("osm_id")val osm_id : Long,
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
    @SerializedName("rank_address") val rank_address: Int,
)

data class PolygonCenter(
    @SerializedName("type") val type: String,
    @SerializedName("coordinates") val centerLatLng: List<Double>
)

data class PolygonGeoJson(
    @SerializedName("type") val type: String,
    @SerializedName("coordinates") val mapsPolygon: Any,
)

data class PolygonData(
    val centerLatLng: LatLng,
    val mapsPolygon: Any,
    val type:String,
    val rankAddress : Int,
){
    companion object{
        const val MULTI_POLYGON = "MultiPolygon"
        const val POLYGON = "Polygon"
    }
}

const val KOREA_SIDO = "korea_sido.json"
const val KOREA_SIGUNGU = "korea_sigungu.json"
data class MapJson(
    @SerializedName("type") val type: String,
    @SerializedName("features") val features: ArrayList<Feature> = arrayListOf()
)

data class Feature(
    @SerializedName("type") val type: FeatureType,
    @SerializedName("geometry") val geometry: Geometry,
    @SerializedName("properties") val properties: Properties
)
data class Geometry (
    @SerializedName("type") val type: GeometryType,
    @SerializedName("coordinates") val coordinates: ArrayList<ArrayList<ArrayList<Any>>> = arrayListOf()
)

data class Properties(
    @SerializedName("CTPRVN_CD")
    val ctprvnCD: String,

    @SerializedName("CTP_ENG_NM")
    val ctpEngNm: String,

    @SerializedName("CTP_KOR_NM")
    val ctpKorNm: String,
)

enum class GeometryType(val value: String) {
    @SerializedName("MultiPolygon") MultiPolygon("MultiPolygon"),
    @SerializedName("Polygon") Polygon("Polygon")
}

enum class FeatureType(val value: String) {
    @SerializedName("Feature") Feature("Feature")
}

data class KoreaPolygon(
    val polygonType: String,
    val coordinates: List<List<Any>>,
    val properties: String,
)