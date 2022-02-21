package com.example.kovid.function.map

import android.annotation.SuppressLint
import android.content.Context
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.util.Log
import com.example.kovid.model.KovidPlace
import com.google.android.gms.maps.model.LatLng
import java.util.*

class KovidLocationManager(private val context: Context){
    val TAG = KovidLocationManager::class.java.name

    lateinit var locationManager: LocationManager

    fun get(): KovidPlace{
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
        if(locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)!= null){
            Log.d(TAG, "Used NETWORK_PROVIDER")
            locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
        }else{
            Log.d(TAG, "Used GPS_PROVIDER")
            locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        }

    // location 변수에서 latitude, longitude 추출
    private fun setMyLocation(location: Location?): KovidPlace {
        return if (location != null) {
            val latLng = LatLng(location.latitude, location.longitude)

            return KovidPlace(getAddress(latLng),"", latLng.latitude, latLng.longitude, location.accuracy)
        } else {
            Log.d(TAG,"location null 임시서울역 구성")

            //임시 서울역
            KovidPlace(
                "KovidLocationManager", "location null 오류",
                37.554891, 126.970814, 0F
            )
        }
    }

    // 위도 경도로 주소 구하는 Reverse-GeoCoding
    private fun getAddress(position: LatLng): String {
        // Geocoder 로 자기 나라에 맞게 설정
        val geoCoder = Geocoder(context, Locale.KOREA)
        var addr = "주소 오류"

        //GRPC 오류? try catch 문으로 오류 대처
        try {
            addr = geoCoder.getFromLocation(position.latitude, position.longitude, 1)
                .first().getAddressLine(0)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return addr
    }

    interface locationManagerCallBack{
        fun onLocationCallBack()
    }
}