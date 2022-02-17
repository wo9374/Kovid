package com.example.kovid.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.kovid.CovidRepository
import com.example.kovid.LocationRepository
import com.example.kovid.model.KovidPlace
import com.google.android.gms.maps.model.Marker
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val TAG = MainViewModel::class.java.simpleName

    private val covidRepo: CovidRepository = CovidRepository()
    private val locationRepo : LocationRepository = LocationRepository(application)

    fun getCovidItem() {
        viewModelScope.launch {
            try {
                val result = covidRepo.getCovidData()
            } catch (e: Exception) {
                Log.d("MainViewModel", "fail...")
            }
        }
    }


    val myLocation = MutableLiveData<KovidPlace>()

    fun getLocation(){
        myLocation.value = locationRepo.getLocation()
    }

    fun permissionCheck() : Boolean{
        return locationRepo.checkFineLocationPermission(getApplication())
                && locationRepo.checkCoarseLocationPermission(getApplication())
    }
}