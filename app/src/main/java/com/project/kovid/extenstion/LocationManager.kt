package com.project.kovid.extenstion

import android.annotation.SuppressLint
import android.content.Context
import android.location.Geocoder
import android.location.LocationManager
import android.os.Looper
import android.util.Log
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.LatLng
import java.util.*

class LocationManager(context: Context) {
    val TAG = LocationManager::class.java.name

    private var mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context) // 현재 위치를 가져오기 위한 변수
    private lateinit var mLocationRequest: LocationRequest // 위치 정보 요청의 매개변수를 저장하는

    @SuppressLint("MissingPermission")
    fun startLocationUpdates(mLocationCallback: LocationCallback) {
        mLocationRequest = LocationRequest.create().setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
        mFusedLocationProviderClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper()!!)
    }

    fun stopLocationUpdates(mLocationCallback: LocationCallback) {
        mFusedLocationProviderClient.removeLocationUpdates(mLocationCallback)
    }
}