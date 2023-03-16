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
/*
data class KoreaSido (
    @SerializedName("type") val type: String,
    @SerializedName("features") val features: ArrayList<Feature> = arrayListOf()
)

data class Feature (
    @SerializedName("type") val type: FeatureType,
    @SerializedName("geometry") val geometry: Geometry,
    @SerializedName("properties") val properties: Properties
)

data class Geometry (
    @SerializedName("type") val type: GeometryType,
    @SerializedName("coordinates") val coordinates: List<List<List<Coordinate>>>
)
*/

data class SiDoModel(
    val siDoCode : Int,
    val siDoName : String,
    val latLngList : List<List<List<Double>>>,
    val siGunGuList : List<SiGunGuModel>
){
    data class SiGunGuModel(
        val siGunGuCode : Int,
        val siGunGuName : String,
        val latLngList : List<List<List<Double>>>
    )
}

data class PolygonInfo(
    val type : String,
    val code : Int,
    val name : String,
    val latLngList : List<List<List<Double>>>
)

/*data class MultiPolygonModel(
    val type : GeometryType,
    val name : String,
    val latLngList : List<List<List<List<Double>>>>
)*/

data class FeatureCollection(
    @SerializedName("type")  val type: String,
    @SerializedName("features") val features: List<Feature>
)

data class Feature(
    @SerializedName("type") val type: String,
    @SerializedName("properties") val properties: Properties,
    @SerializedName("geometry") val geometry: Geometry,
)

data class Geometry(
    @SerializedName("type") val type: String,
    @SerializedName("coordinates") val coordinates: List<List<List<Double>>>,
)

data class Properties (
    @SerializedName(value = "CTPRVN_CD", alternate=["SIG_CD"]) val ctpRvnCD: String,
    @SerializedName(value = "CTP_ENG_NM", alternate=["SIG_ENG_NM"]) val ctpEngNm: String,
    @SerializedName(value = "CTP_KOR_NM", alternate=["SIG_KOR_NM"]) val ctpKorNm: String
)