package com.project.kovid.viewmodel

import android.Manifest
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.maps.model.LatLng
import com.project.kovid.function.repository.MapRepository
import com.project.kovid.model.HospDBItem
import com.project.kovid.widget.extension.LocationManager
import com.project.kovid.widget.util.LocationUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class MapsViewModel(application: Application) : AndroidViewModel(application) {
    private val TAG = MapsViewModel::class.java.simpleName
    val context: Application = application

    private val mapRepo: MapRepository = MapRepository(application)

    var symptomTestHospData = MutableLiveData<List<HospDBItem>>()

    var detailAddress = Pair("","") //계속해서 받아오는

    /**
     *현위치 get
     * */
    private val _currentLocation = MutableSharedFlow<Location>(
        replay = 0,                         //새로운 구독자에게 이전 이벤트를 전달하지 않음
        extraBufferCapacity = 1,            //추가 버퍼를 생성하여 emit 한 데이터가 버퍼에 유지되도록 함
        onBufferOverflow = BufferOverflow.DROP_OLDEST   //버퍼가 가득찼을 시 오래된 데이터 제거
    )
    val currentLocation = _currentLocation.asSharedFlow()

    private val locationLoader = LocationManager(application)

    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            viewModelScope.launch {
                locationResult.lastLocation.apply {
                    //현재 위도,경도 Flow 방출 (GoogleMap UI 조작)
                    _currentLocation.emit(this)

                    //현재 위도,경도로 지오코딩을 통해 주소를 얻어옴
                    LocationUtil(context).getReverseGeocoding(LatLng(latitude, longitude)).let { address ->
                        address.split(" ").let { detail ->
                            val sido = detail[1]
                            val sigungu = detail[2]
                            Log.d(TAG, "시도명 : $sido, 시군구명 : $sigungu, 전체주소 : $address")

                            //시도와 시군구가 동일하지 않을때 Data 새로 Load
                            if (detailAddress != Pair(sido, sigungu)){
                                detailAddress = Pair(sido, sigungu)
                                getData()
                            }
                        }
                    }

                }
            }
        }
    }
    fun startLocation() = locationLoader.startLocationUpdates(mLocationCallback)
    fun stopLocation() = locationLoader.stopLocationUpdates(mLocationCallback)
    fun checkLocationPermission(): Boolean =
        ActivityCompat.checkSelfPermission(getApplication(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getApplication(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED


    /**
     *병원정보 get
     * */
    fun getData(){
        viewModelScope.launch {
            //TODO 1.현위치 해당 병원 정보가 있는지 DB 체크
            //TODO 2. DB에 있을시 DB에서 DataLoad
            //TODO 3. DB에도 없을시 API 호출해 Remote Data 받아오기
            //TODO 4. 받아온 Remote Data Ui 표시, DB 저장

            try {
                val result = mapRepo.getSymptomTest(detailAddress.first, detailAddress.second)
                if (result.isSuccessful && result.body() != null) {
                    val resultData = result.body()!!

                    /*resultData.forEachIndexed { index, hospItem ->
                        hospItem.run {
                            insert(HospDBItem(index, addr, recuClCd, pcrPsblYn, ratPsblYn, sgguCdNm, sidoCdNm, telno, yadmNm, YPosWgs84, XPosWgs84))
                        }
                    }*/
                    //Log.d(TAG, "getHospData() Room InsertSuccess ${getAll()}")
                    //symptomTestHospData.postValue(getAll())
                } else {
                    Log.d(TAG, "getHospData() result not Successful or result.body null")
                }

            } catch (e: Exception) {
                Log.d(TAG, "getHospData() fail...")
            }
        }
    }


    /**
     * Room
     */
    fun getAll(): List<HospDBItem> {
        return mapRepo.getAll()
    }

    fun insert(hospDBItem: HospDBItem) {
        mapRepo.insert(hospDBItem)
    }

    override fun onCleared() {
        super.onCleared()
    }
}