package com.project.kovid.widget.extension

import android.annotation.SuppressLint
import android.content.Context
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Looper
import android.util.Log
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
    fun geoCoding(address: String): Location {
        return try {
            Geocoder(context, Locale.KOREA).getFromLocationName(address, 1)?.let{
                Location("").apply {
                    latitude =  it[0].latitude
                    longitude = it[0].longitude
                }
            }?: Location("").apply {
                latitude = 0.0
                longitude = 0.0
            }
        }catch (e:Exception) {
            e.printStackTrace()

            Location("").apply {
                latitude = 0.0
                longitude = 0.0
            }
        }
    }

    // 위도 경도로 주소 구하는 Reverse-GeoCoding
    fun reverseGeoCoding(location: Location) : String{
        return try {
            // Geocoder 로 자기 나라에 맞게 설정
            with(Geocoder(context, Locale.KOREA).getFromLocation(location.latitude, location.longitude, 1).first()){
                adminArea //ex. 서울특별시,
            }
        } catch (e: Exception) { //GRPC 오류시 재시도
            e.printStackTrace()
            reverseGeoCoding(location)
        }
    }

    fun tt(location: Location){
        Geocoder(context, Locale.KOREA).getFromLocation(location.latitude, location.longitude, 1)
    }
}
