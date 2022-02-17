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

    fun getLocation(){
        covidRepo.getLocation()
    }

    fun permissionCheck() : Boolean{
        return covidRepo.checkCoarseLocationPermission() && covidRepo.checkFineLocationPermission()
    }
}