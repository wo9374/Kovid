package com.example.kovid.view

import android.annotation.SuppressLint
import android.content.Context
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import com.google.android.gms.maps.model.LatLng
import java.util.*

class KovidLocationManager(private val context: Context){
    lateinit var locationManager: LocationManager

    fun get(): String{
        setLocationManager()
        return setMyLocation(getLastLocation())
    }

    // locationManager 초기화
    private fun setLocationManager() {
        locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    }

    // Permission 은 해당 클래스를 사용해주는 Class 에서 처리, 따라서 MissingPermission 을 Suppress.
    @SuppressLint("MissingPermission")
    private fun getLastLocation(): Location? =
        if (locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER) != null) {
            locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        } else {
            locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
        }

    // location 변수에서 latitude, longitude 추출
    private fun setMyLocation(location: Location?): String {
        return if (location != null) {
            val latLng = LatLng(location.latitude, location.longitude)
            return getAddress(latLng)
        } else {
            "주소정보 가져올 수 없음"
        }
    }

    // Geocoder 로 자기 나라에 맞게 설정, GRPC 오류? try catch 문으로 오류 대처
    private fun getAddress(position: LatLng): String {
        val geoCoder = Geocoder(context, Locale.KOREA)
        var addr = "주소 오류"

        try {
            addr = geoCoder.getFromLocation(
                position.latitude,
                position.longitude,
                1)
                .first().getAddressLine(0)

        } catch (e: Exception) {
            e.printStackTrace()
        }
        return addr
    }
}