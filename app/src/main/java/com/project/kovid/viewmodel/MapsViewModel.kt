package com.project.kovid.viewmodel

import android.location.Location
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.maps.model.LatLng
import com.ljb.data.mapper.mapperToCluster
import com.ljb.data.model.SelectiveCluster
import com.ljb.data.util.splitSido
import com.ljb.domain.NetworkState
import com.ljb.domain.UiState
import com.ljb.domain.usecase.ClearSelectiveClinicUseCase
import com.ljb.domain.usecase.GetDbSelectiveClinicUseCase
import com.ljb.domain.usecase.GetSelectiveClinicUseCase
import com.ljb.domain.usecase.InsertSelectiveClinicUseCase
import com.project.kovid.widget.extension.MyLocationManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MapsViewModel @Inject constructor(
    private val getSelectiveClinicUseCase: GetSelectiveClinicUseCase,
    private val locationManager: MyLocationManager,

    private val getDbSelectiveClinicUseCase: GetDbSelectiveClinicUseCase,
    private val insertSelectiveClinicUseCase: InsertSelectiveClinicUseCase,
    private val clearSelectiveClinicUseCase: ClearSelectiveClinicUseCase,
) : ViewModel() {
    private val tag = MapsViewModel::class.java.simpleName

    private val _hospitalClusters = MutableStateFlow<UiState<List<SelectiveCluster>>>(UiState.Loading)
    val hospitalClusters: StateFlow<UiState<List<SelectiveCluster>>> get() = _hospitalClusters

    /**
     *현위치 get
     * */
    private val _currentLocation = MutableSharedFlow<Location>(
        replay = 0,                         //새로운 구독자에게 이전 이벤트를 전달하지 않음
        extraBufferCapacity = 1,            //추가 버퍼를 생성하여 emit 한 데이터가 버퍼에 유지되도록 함
        onBufferOverflow = BufferOverflow.DROP_OLDEST   //버퍼가 가득찼을 시 오래된 데이터 제거
    )
    val currentLocation = _currentLocation.asSharedFlow()

    var detailAddress = Pair("", "") //주소가 바꼈을때를 판단하기 위한 Pair

    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            locationResult.lastLocation.apply {
                locationManager.getReverseGeocoding(LatLng(latitude, longitude)).let { address ->
                    address.split(" ").let { detail ->
                        val siDo = detail[1].splitSido()
                        val siGunGu = ""//detail[2]

                        //시도와 시군구가 동일하지 않을때 Data 새로 Load
                        if (detailAddress != Pair(siDo, siGunGu)) {
                            detailAddress = Pair(siDo, siGunGu)
                            getDbData()
                        }
                        Log.d(tag, "getData($siDo, $siGunGu) 전체주소 : $address")

                        viewModelScope.launch { _currentLocation.emit(locationResult.lastLocation) }
                    }
                }
            }
        }
    }

    fun startLocation() = locationManager.startLocationUpdates(mLocationCallback)
    fun stopLocation() = locationManager.stopLocationUpdates(mLocationCallback)


    fun getDbData() {
        viewModelScope.launch {
            withContext(Dispatchers.IO){

                getDbSelectiveClinicUseCase().apply {
                    if (isEmpty()) {
                        //DB Data 없을시 Remote API 호출
                        getRemoteData(detailAddress.first, detailAddress.second)
                    } else {
                        _hospitalClusters.emit(UiState.Complete(map {
                            it.mapperToCluster(locationManager.getGeoCoding(it.addr))
                        }))
                    }
                }
            }
        }
    }

    fun getRemoteData(siDo: String, siGunGu: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {

                getSelectiveClinicUseCase(siDo, siGunGu)
                    .catch { exception ->
                        Log.d(tag, "SelectiveClinic Exception Error: ${exception.message}")
                    }.collect { result ->
                        when (result) {
                            is NetworkState.Success -> {
                                //Data UI 마커 먼저 처리
                                val cluster = result.data.map { it.mapperToCluster(locationManager.getGeoCoding(it.addr)) }
                                _hospitalClusters.emit(UiState.Complete(cluster))
                                Log.d(tag, "SelectiveClinic Success: Total${result.data.size} ${result.data}")

                                //이후 DB 저장
                                result.data.forEach { insertSelectiveClinicUseCase(it) }
                            }
                            is NetworkState.Error -> {
                                _hospitalClusters.emit(UiState.Fail(result.message))
                                Log.d(tag, "SelectiveClinic Error : ${result.message}")
                            }
                            is NetworkState.Loading -> {
                                _hospitalClusters.emit(UiState.Loading)
                            }
                        }
                    }
            }
        }
    }
}