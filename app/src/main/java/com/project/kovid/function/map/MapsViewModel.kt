package com.project.kovid.function.map

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.project.kovid.model.HospItem
import com.project.kovid.model.Place
import com.project.kovid.repository.MapRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MapsViewModel(application: Application) : AndroidViewModel(application) {
    private val TAG = MapsViewModel::class.java.simpleName

    private val mapRepo: MapRepository = MapRepository(application)

    val myLocation = MutableLiveData<Place>()

    //현위치 가져오기
    fun getLocation() {
        myLocation.value = mapRepo.getLocation()
    }

    //위치 퍼미션 체크
    fun permissionCheck(): Boolean {
        return mapRepo.checkFineLocationPermission(getApplication()) && mapRepo.checkCoarseLocationPermission(getApplication())
    }


    var hospData = MutableLiveData<List<HospItem>>()

    fun getHospital() {
        CoroutineScope(Dispatchers.IO).launch{
            try {
                val result = mapRepo.getHospitalData()
                if (result.isSuccessful && result.body() != null) {
                    hospData.postValue(result.body()?.body?.items?.item)
                    Log.d(TAG, "병원데이터 ${result.body()?.body?.items?.item}")
                } else {
                    Log.d(TAG, "getHospital() result not Successful or result.body null")
                }
            } catch (e: Exception) {
                Log.d(TAG, "getHospital() fail...")
            }
        }
    }
}