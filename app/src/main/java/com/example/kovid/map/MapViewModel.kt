package com.example.kovid.map

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.kovid.model.KovidPlace

class MapViewModel(application: Application) : AndroidViewModel(application) {
    private val TAG = MapViewModel::class.java.simpleName

    private val locationRepo : LocationRepository = LocationRepository(application)
    val myLocation = MutableLiveData<KovidPlace>()

    fun getLocation(){
        myLocation.value = locationRepo.getLocation()
    }

    fun permissionCheck() : Boolean{
        return locationRepo.checkFineLocationPermission(getApplication())
                && locationRepo.checkCoarseLocationPermission(getApplication())
    }
}