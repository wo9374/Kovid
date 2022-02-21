package com.example.kovid.function.map

import android.Manifest
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import com.example.kovid.model.KovidPlace

class LocationRepository(application: Application) {
    private val locationLoader = KovidLocationManager(application)

    fun getLocation() : KovidPlace{
        return locationLoader.get()
    }

    fun checkFineLocationPermission(context: Context): Boolean =
        ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED

    fun checkCoarseLocationPermission(context: Context): Boolean =
        ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
}