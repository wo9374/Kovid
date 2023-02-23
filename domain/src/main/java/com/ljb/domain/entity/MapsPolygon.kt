package com.ljb.domain.entity

data class MapsPolygon(
    val centerLatLng : List<Double>,
    val mapsPolygon : List<List<Double>>,
)