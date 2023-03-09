package com.ljb.data.mapper

import com.google.android.gms.maps.model.LatLng
import com.ljb.data.model.GeometryType
import com.ljb.data.model.KoreaPolygon
import com.ljb.data.model.MapJson
import com.ljb.data.model.PolygonData
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
fun MapJson.mapperToMapsInfo(): List<MapsInfo>{
    return features.mapNotNull { feature ->  // mapNotNull을 사용하여 nullable한 값 제거
        with(feature){
            geometry.coordinates.safeCast<List<List<List<List<Double>>>>>()?.let { cast ->
                val multiPolygon = cast.map { list ->
                    list.forEach { multi->
                        multi.forEach {
                            LatLng(it[1], it[0])
                        }
                    }
                }
                MapsInfo(
                    polygonType = type.value,
                    coordinates = multiPolygon,
                    properties = properties.ctpKorNm,
                )

            } ?: geometry.coordinates.safeCast<List<List<List<Double>>>>()?.let { cast ->
                val polygons = cast.map { list->  // mapNotNull을 사용하여 nullable한 값 제거
                    list.map {
                        LatLng(it[1], it[0])
                    }
                }

                MapsInfo(
                    polygonType = geometry.type.value,
                    coordinates = polygons,
                    properties = properties.ctpKorNm,
                )
            }
        }
    }
}