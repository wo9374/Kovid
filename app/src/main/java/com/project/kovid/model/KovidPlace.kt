package com.project.kovid.model

data class KovidPlace(
    val placeAddr: String,
    val placeDetailAddr: String,
    var placeLatitude: Double,   // Y 위도
    var placeLongitude: Double,  // X 경도
    var placeAccuracy: Float,    // 마커 Circle 값
)
