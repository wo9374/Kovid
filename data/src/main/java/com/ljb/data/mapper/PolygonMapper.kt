package com.ljb.data.mapper

import com.google.android.gms.maps.model.LatLng

fun List<Double>.mapperToLatLng() = LatLng(this[1], this[0])