package com.project.kovid.widget.extension

import android.annotation.SuppressLint
import android.content.Context
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Looper
import com.google.android.gms.location.*
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.*
import javax.inject.Inject

/**
 * Provide [AppModule]
 * */
class MyLocationManager @Inject constructor(@ApplicationContext val context: Context) {
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
    //주소로 위도,경도 구하는 GeoCoding
    @Suppress("DEPRECATION")
    fun getGeocoding(address: String): Location{
        try {
            var location = Location("")

            with(Geocoder(context, Locale.KOREA)){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
                    getFromLocationName(address, 1) {
                        it.firstOrNull()?.let {
                            location = Location("").apply {
                                latitude = it.latitude
                                longitude = it.longitude
                            }
                        }
                    }
                }else{
                    getFromLocationName(address, 1)?.first()?.let {
                        location = Location("").apply {
                            latitude = it.latitude
                            longitude = it.longitude
                        }
                    }
                }
            }
            return location
        }catch (e:Exception){
            e.printStackTrace()
            return Location("")
        }
    }

    // 위도 경도로 주소 구하는 Reverse-GeoCoding
    @Suppress("DEPRECATION")
    fun getReverseGeocoding(location: Location): String{
        try {
            var str = ""

            with(Geocoder(context, Locale.KOREA)){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
                    getFromLocation(location.latitude, location.longitude, 1){
                        it.firstOrNull()?.let { addr ->
                            str = addr.adminArea
                        }
                    }
                }else{
                    Geocoder(context, Locale.KOREA).getFromLocation(location.latitude, location.longitude, 1)?.first()?.let {
                        str = it.adminArea
                    }
                }
            }
            return str
        }catch (e:Exception){
            e.printStackTrace()
            return ""
        }
    }
}
