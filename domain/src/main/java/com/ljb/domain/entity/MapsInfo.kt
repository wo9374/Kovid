package com.ljb.domain.entity

data class MapsInfo(
    val polygonType: String,
    val coordinates: List<Any>,
    val properties: String,
)