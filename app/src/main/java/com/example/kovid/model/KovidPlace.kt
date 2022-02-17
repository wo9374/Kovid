package com.example.kovid.model

data class KovidPlace(
    val placeAddr : String,
    val placeDetailAddr : String,
    var placeLatitude : Double,   // Y 위도
    var placeLongitude : Double // X 경도
)
