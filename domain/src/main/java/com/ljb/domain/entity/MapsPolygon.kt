package com.ljb.domain.entity

data class MapsPolygon(
    val polygon : List<List<Double>>,
    val centerLatLng : List<Double>,
)