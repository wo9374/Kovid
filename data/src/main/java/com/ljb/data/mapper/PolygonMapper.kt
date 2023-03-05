package com.ljb.data.mapper

import com.google.android.gms.maps.model.LatLng
import com.ljb.data.model.PolygonData
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