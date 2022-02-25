package com.project.kovid.function.repository

import android.Manifest
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import com.project.kovid.BuildConfig
import com.project.kovid.function.map.HospitalAPI
import com.project.kovid.extenstion.LocationManager
import com.project.kovid.model.HospData
import com.project.kovid.objects.RetrofitObject
import retrofit2.Response

class MapRepository(application: Application) {
    private val hospitalRetrofit : retrofit2.Retrofit = RetrofitObject.getRetrofitHospital()
    private val hospitalApi = hospitalRetrofit.create(HospitalAPI::class.java)

    suspend fun getHospitalData() : Response<HospData> = hospitalApi.getHospital(BuildConfig.HOSPITAL_API_KEY,1, 102, "97")



    val locationLoader = LocationManager(application)

    fun startLocation(mLocationCallback: LocationCallback){
        locationLoader.startLocationUpdates(mLocationCallback)
    }

    fun stopLocation(mLocationCallback: LocationCallback){
        locationLoader.stopLocationUpdates(mLocationCallback)
    }

    fun checkFineLocationPermission(context: Context): Boolean =
        ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED

    fun checkCoarseLocationPermission(context: Context): Boolean =
        ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
}