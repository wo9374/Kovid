package com.project.kovid.widget.util

import android.content.Context
import android.location.Geocoder
import android.util.Log
import com.google.android.gms.maps.model.LatLng
import java.util.*

/**
 * 위치에 관한 함수 Util
 * */
class LocationUtil(val context: Context?) {

    //주소로 위도,경도 구하는 GeoCoding
    fun getGeoCoding(address: String): LatLng {
        val geoCoder = Geocoder(context, Locale.KOREA)
        val list = geoCoder.getFromLocationName(address, 3)

        var location = LatLng(37.554891, 126.970814) //임시 서울역

        if (list != null) {
            if (list.size == 0) {
                Log.d("GeoCoding", "해당 주소로 찾는 위도 경도가 없습니다. 올바른 주소를 입력해주세요.")
            } else {
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
            addr = geoCoder.getFromLocation(position.latitude, position.longitude, 3).first()
                .getAddressLine(0)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return addr
    }
}