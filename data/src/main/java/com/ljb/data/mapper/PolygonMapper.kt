package com.ljb.data.mapper

import com.google.android.gms.maps.model.LatLng
import com.ljb.data.model.PolygonData
import com.ljb.domain.entity.MapsPolygon

fun MapsPolygon.mapperToLatLng() = PolygonData(
    centerLatLng = LatLng(centerLatLng[1], centerLatLng[0]),
    polygonLatLng = mapsPolygon.map {
        LatLng(it[1], it[0])
    }
)