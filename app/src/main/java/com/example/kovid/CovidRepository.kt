package com.example.kovid

import android.Manifest
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.lifecycle.MutableLiveData
import com.example.kovid.model.KoreaCovidCount
import com.example.kovid.model.KovidPlace
import com.example.kovid.view.KovidLocationManager
import retrofit2.Response
import retrofit2.Retrofit

class CovidRepository(application: Application){

    private val covidRetrofit : Retrofit = RetrofitService.getRetrofitXmlParsing()

    private val api = covidRetrofit.create(CovidAPI::class.java)

    suspend fun getCovidData() : Response<KoreaCovidCount>? = api.getInfo()



    private val locationLoader = KovidLocationManager(application)
    private val myLocation = MutableLiveData<KovidPlace>()

    fun getLocation() {
        myLocation.value = locationLoader.get()
    }

    fun checkFineLocationPermission(context: Context): Boolean =
        ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED

    fun checkCoarseLocationPermission(context: Context): Boolean =
        ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
}