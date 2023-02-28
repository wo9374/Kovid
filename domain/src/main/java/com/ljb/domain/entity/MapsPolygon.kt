package com.ljb.domain.entity

data class MapsPolygon(
    val centerLatLng : List<Double>,
    val mapsPolygon : Any,
    val type: String,
    val rankAddress : Int,
)