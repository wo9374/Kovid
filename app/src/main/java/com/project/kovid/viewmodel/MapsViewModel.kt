package com.project.kovid.viewmodel

import android.location.Location
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.maps.model.LatLng
import com.ljb.data.mapper.mapperToCluster
import com.ljb.data.remote.model.SelectiveCluster
import com.ljb.domain.NetworkState
import com.ljb.domain.usecase.GetSelectiveClinicUseCase
import com.project.kovid.widget.extension.MyLocationManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapsViewModel @Inject constructor(
    private val getSelectiveClinicUseCase: GetSelectiveClinicUseCase,
    private val locationManager: MyLocationManager,
) : ViewModel() {
    private val tag = MapsViewModel::class.java.simpleName

    private val _hospitalClusters = MutableStateFlow<List<SelectiveCluster>>(mutableListOf())
    val hospitalClusters : StateFlow<List<SelectiveCluster>> get() = _hospitalClusters
    /**
     *병원정보 get
     * */
    fun getData(){
        viewModelScope.launch {
            getSelectiveClinicUseCase()
                .catch { exception ->
                    Log.d(tag, "HospitalData - Exception Error : ${exception.message}")
                }.collect{ result->
                    when(result){
                        is NetworkState.Success -> {
                            Log.d(tag, "HospitalData - Network Success : ${result.data}")
                            val cluster = result.data.map { it.mapperToCluster(locationManager.getGeoCoding(it.addr)) }
                            _hospitalClusters.emit(cluster)
                        }
                        is NetworkState.Error -> {
                            Log.d(tag, "HospitalData - Network Error : ${result.message}")
                        }
                        else -> {}
                    }
                }
        }
    }

    /**
     *현위치 get
     * */
    private val _currentLocation = MutableSharedFlow<Location>(
        replay = 0,                         //새로운 구독자에게 이전 이벤트를 전달하지 않음
        extraBufferCapacity = 1,            //추가 버퍼를 생성하여 emit 한 데이터가 버퍼에 유지되도록 함
        onBufferOverflow = BufferOverflow.DROP_OLDEST   //버퍼가 가득찼을 시 오래된 데이터 제거
    )
    val currentLocation = _currentLocation.asSharedFlow()

    var detailAddress = Pair("","") //주소가 바꼈을때를 판단하기 위한 Pair

    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            viewModelScope.launch {
                _currentLocation.emit(locationResult.lastLocation)
            }
            locationResult.lastLocation.apply {
                //현재 위도,경도로 지오코딩을 통해 주소를 얻어옴
                locationManager.getReverseGeocoding(LatLng(latitude, longitude)).let { address ->
                    address.split(" ").let { detail ->
                        val sido = detail[1]
                        val sigungu = detail[2]
                        Log.d(tag, "시도명 : $sido, 시군구명 : $sigungu, 전체주소 : $address")

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
    fun startLocation() = locationManager.startLocationUpdates(mLocationCallback)
    fun stopLocation() = locationManager.stopLocationUpdates(mLocationCallback)
}