package com.project.kovid.function.map

import android.annotation.SuppressLint
import android.content.Context
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.util.Log
import com.project.kovid.model.Place
import com.google.android.gms.maps.model.LatLng
import com.project.kovid.model.HospItem
import java.util.*

class KovidLocationManager(private val context: Context){
    val TAG = LocationManager::class.java.name

    lateinit var locationManager: LocationManager

    fun get(): Location?{
        setLocationManager()
        return getLastLocation()
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
    private fun setMyLocation(location: Location?): LatLng {
        return if (location != null) {
            LatLng(location.latitude, location.longitude)
        } else {
            Log.d(TAG,"location null 임시서울역 구성")
            LatLng(37.554891, 126.970814)  //임시 서울역
        }
    }

    //주소로 위도,경도 구하는 GeoCoding
    fun getGeoCoding(address:String) : LatLng{
        val geoCoder = Geocoder(context, Locale.KOREA)
        val list = geoCoder.getFromLocationName(address, 3)

        var location = LatLng(37.554891, 126.970814) //임시 서울역

        if(list != null){
            if (list.size ==0){
                Log.d("GeoCoding", "해당 주소로 찾는 위도 경도가 없습니다. 올바른 주소를 입력해주세요.")
            }else{
                val addressLatLng = list[0]
                location = LatLng(addressLatLng.latitude, addressLatLng.longitude)
                return location
            }
        }

        return location
    }

    // 위도 경도로 주소 구하는 Reverse-GeoCoding
    fun getReverseGeocoding(position: LatLng): String {
        // Geocoder 로 자기 나라에 맞게 설정
        val geoCoder = Geocoder(context, Locale.KOREA)
        var addr = "주소 오류"

        //GRPC 오류? try catch 문으로 오류 대처
        try {
            addr = geoCoder.getFromLocation(position.latitude, position.longitude, 3).first().getAddressLine(0)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return addr
    }

    fun getGeoCodingList(list : List<HospItem>) : List<LatLng>{
        val listLatLng = mutableListOf<LatLng>()

        list.forEachIndexed { index, hospItem ->
            if (hospItem.yadmNm.isNullOrEmpty()){
                return emptyList()
            }else{
                listLatLng.add(getGeoCoding(hospItem.yadmNm!!))
            }
        }
        return listLatLng
    }
}