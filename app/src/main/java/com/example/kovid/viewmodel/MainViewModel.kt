package com.example.kovid.viewmodel

import android.Manifest
import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.databinding.ObservableField
import androidx.lifecycle.*
import com.example.kovid.CovidRepository
import com.example.kovid.model.KovidPlace
import com.example.kovid.view.KovidLocationManager
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val TAG = MainViewModel::class.java.simpleName
    private val covidRepo: CovidRepository = CovidRepository(application)

    fun getCovidItem() {
        viewModelScope.launch {
            try {
                val result = covidRepo.getCovidData()
            } catch (e: Exception) {
                Log.d("MainViewModel", "fail...")
            }
        }
    }




    private val currentPlace: KovidPlace = KovidPlace("서울역", "", 37.554891, 126.970814)
    private var gpsListener: KovidLocationManager = KovidLocationManager(application)

    companion object {
        private const val MIN_DISTANCE_CHANGE_FOR_UPDATES: Long = 10
        private const val MIN_TIME_BW_UPDATES = (1000 * 10 * 1).toLong()
    }

    /*fun setLocationGps() {
      try {
          val locationManager = context.getSystemService(Context.LOCATION_SERVICE)
          val isGPSEnabled = locationManager?.isProviderEnabled(LocationManager.GPS_PROVIDER)
          val isNetworkEnabled =
              locationManager?.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

          var location: Location? = null
          if (!isGPSEnabled && !isNetworkEnabled) {
          } else {
              val hasFineLocationPermission = ContextCompat.checkSelfPermission(
                  context,
                  Manifest.permission.ACCESS_FINE_LOCATION
              )
              println("hasFineLocationPermission: $hasFineLocationPermission")

              if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED) {
                  if (isNetworkEnabled) {
                      locationManager.requestLocationUpdates(
                          LocationManager.NETWORK_PROVIDER,
                          MIN_TIME_BW_UPDATES,
                          MIN_DISTANCE_CHANGE_FOR_UPDATES.toFloat(),
                          gpsListener as LocationListener
                      )
                      if (locationManager != null) {
                          location =
                              locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                          if (location != null) {
                              currentPlace.placeLatitude = location.latitude
                              currentPlace.placeLongitude = location.longitude //경도
                          }
                      }
                  }
                  if (isGPSEnabled) {
                      if (location == null) {
                          locationManager.requestLocationUpdates(
                              LocationManager.GPS_PROVIDER,
                              MIN_TIME_BW_UPDATES,
                              MIN_DISTANCE_CHANGE_FOR_UPDATES.toFloat(),
                              gpsListener as LocationListener
                          )
                          if (locationManager != null) {
                              location =
                                  locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                              if (location != null) {
                                  currentPlace.placeLatitude = location.latitude
                                  currentPlace.placeLongitude = location.longitude //경도
                              }
                          }
                      }
                  }
              }
          }
      } catch (e: java.lang.Exception) {
          Log.d("@@@", "" + e.toString())
      }
  }*/

    fun getCurrentPlace(): KovidPlace {
        return currentPlace
    }





    private val myLocation: ObservableField<String> = ObservableField("주소를 표시할 수 없습니다.")

    fun checkFineLocationPermission(context: Context): Boolean =
        ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED

    fun checkCoarseLocationPermission(context: Context): Boolean =
        ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED


    @SuppressLint("MissingPermission")
    fun getLocation(context: Context) {
        val locationLoader = KovidLocationManager(context)
        myLocation.set(locationLoader.get())
    }
}