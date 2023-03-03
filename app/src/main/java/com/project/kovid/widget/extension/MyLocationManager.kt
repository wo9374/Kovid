package com.project.kovid.widget.extension

import android.annotation.SuppressLint
import android.content.Context
import android.location.Geocoder
import android.location.Location
import android.os.Build
import android.os.Looper
import com.google.android.gms.location.*
import com.ljb.data.util.containsMetropolitanCity
import com.ljb.data.util.splitSi
import com.ljb.koreaarea.KoreaAreaSpinner
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject

/**
 * Provide [AppModule]
 * */
class MyLocationManager @Inject constructor(@ApplicationContext val context: Context) {

    private var mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
    private lateinit var mLocationRequest: LocationRequest

    @SuppressLint("MissingPermission")
    fun startLocationUpdates(mLocationCallback: LocationCallback) {
        mLocationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 2000)
            .setWaitForAccurateLocation(false)  //위치 요청할 때 정확한 위치를 기다리는 기능 설정
            .setMinUpdateIntervalMillis(2500)   //위치 업데이트를 요청하는데 사용되는 최소 업데이트 간격
            .setMaxUpdateDelayMillis(1000)      //위치 업데이트를 받기 전에 시스템이 대기하는 최대 시간을 설정
            .build()
        mFusedLocationProviderClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper()!!)
    }

    fun stopLocationUpdates(mLocationCallback: LocationCallback) {
        mFusedLocationProviderClient.removeLocationUpdates(mLocationCallback)
    }

    //주소로 위도,경도 구하는 GeoCoding
    @Suppress("DEPRECATION")
    fun getGeocoding(address: String): Location {
        try {
            var location = Location("")

            with(Geocoder(context, Locale.KOREA)) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    getFromLocationName(address, 1) {
                        it.firstOrNull()?.let {
                            location = Location("").apply {
                                latitude = it.latitude
                                longitude = it.longitude
                            }
                        }
                    }
                } else {
                    getFromLocationName(address, 1)?.first()?.let {
                        location = Location("").apply {
                            latitude = it.latitude
                            longitude = it.longitude
                        }
                    }
                }
            }
            return location
        } catch (e: Exception) {
            e.printStackTrace()
            return Location("")
        }
    }

    // 위도 경도로 주소 구하는 Reverse-GeoCoding
    @Suppress("DEPRECATION")
    suspend fun getReverseGeocoding(location: Location): Pair<String, String> {
        var pair: Pair<String, String> = Pair("", "")

        withContext(Dispatchers.IO) {
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    Geocoder(context, Locale.KOREA).getFromLocation(location.latitude, location.longitude, 1) {
                        with(it.first()) {
                            pair = if (adminArea.containsMetropolitanCity())
                                Pair(adminArea.splitSi(), KoreaAreaSpinner.regionAll)
                            else
                                Pair(adminArea, locality)
                        }
                    }
                } else {
                    Geocoder(context, Locale.KOREA).getFromLocation(location.latitude, location.longitude, 1)?.first()?.apply {
                        pair = if (adminArea.containsMetropolitanCity())
                            Pair(adminArea.splitSi(), KoreaAreaSpinner.regionAll)
                        else
                            Pair(adminArea, locality)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                getReverseGeocoding(location)
            }
        }
        return pair
    }
}
