package com.ljb.data.mapper

import android.util.Log
import com.google.android.gms.maps.model.LatLng
import com.google.gson.JsonElement
import com.ljb.data.model.Feature
import com.ljb.data.model.PolygonData
import com.ljb.data.model.PolygonInfo
import com.ljb.domain.entity.MapsInfo
import com.ljb.domain.entity.MapsPolygon

fun MapsPolygon.mapperToLatLng() = PolygonData(
    centerLatLng = LatLng(centerLatLng[1], centerLatLng[0]),
    mapsPolygon = when(type){
        PolygonData.MULTI_POLYGON->{
            (mapsPolygon as List<List<List<List<Double>>>>).map { multi ->
                multi[0].map {
                    LatLng(it[1], it[0])
                }
            }
        }
        else -> { //Polygon
            (mapsPolygon as List<List<List<Double>>>)[0].map {
                LatLng(it[1], it[0])
            }
        }
    },
    type = type,
    rankAddress = rankAddress,
)

inline fun <reified T> Any?.safeCast(): T? = this as? T

/*fun Coordinate.mapperToLatLngAny(): Coordinate? {
    return safeCast<List<List<List<List<Double>>>>>()?.let { coordinates ->
        coordinates.forEach { coordinate ->
            coordinate.forEach { latLngList ->
                latLngList.map {
                    LatLng(it[1],it[0])
                }
            }
        }
        Coordinate.MultiPolygonValue(coordinates)
    } ?: safeCast<List<List<List<Double>>>>()?.let { coordinates->
        coordinates.forEach { coordinate ->
            coordinate.map {
                LatLng(it[1],it[0])
            }
        }
        Coordinate.PolygonValue(coordinates)
    }
}*/

fun JsonElement.parsingMapData(siDoType : Boolean) : PolygonInfo {
    val geo = asJsonObject["geometry"]
    val polygonType = geo.asJsonObject["type"].asString
    val properties = asJsonObject["properties"].asJsonObject

    val code = if (siDoType) properties["CTPRVN_CD"].asString.toInt() else properties["SIG_CD"].asString.toInt()
    val name = if (siDoType) properties["CTP_KOR_NM"].asString else properties["SIG_KOR_NM"].asString

    val coordinate = geo.asJsonObject["coordinates"]
    val latLngList = when(polygonType){
        PolygonData.POLYGON -> {
            coordinate.asJsonArray[0].asJsonArray.map {
                //MultiPolygon 과 동일한 Data Class 를 위한 List 로 한번더 래핑
                listOf(
                    listOf(it.asJsonArray[1].asDouble, it.asJsonArray[0].asDouble)
                )
            }
        }
        //MULTI_POLYGON
        else -> {
            coordinate.asJsonArray.map { multi ->
                multi.asJsonArray[0].asJsonArray.map {
                    listOf(it.asJsonArray[1].asDouble, it.asJsonArray[0].asDouble)
                }
            }
        }
    }
    return PolygonInfo(type = polygonType, code = code, name, latLngList = latLngList)
}