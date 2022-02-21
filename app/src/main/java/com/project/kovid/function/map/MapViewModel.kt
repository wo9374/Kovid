package com.project.kovid.function.map

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.project.kovid.model.Place

class MapViewModel(application: Application) : AndroidViewModel(application) {
    private val TAG = MapViewModel::class.java.simpleName

    private val kovidLocationRepo : KovidLocationRepository = KovidLocationRepository(application)

    val myLocation = MutableLiveData<Place>()

    fun getLocation(){
        myLocation.value = kovidLocationRepo.getLocation()
    }

    fun permissionCheck() : Boolean{
        return kovidLocationRepo.checkFineLocationPermission(getApplication())
                && kovidLocationRepo.checkCoarseLocationPermission(getApplication())
    }
}