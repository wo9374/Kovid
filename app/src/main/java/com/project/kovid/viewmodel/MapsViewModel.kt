package com.project.kovid.viewmodel

import android.location.Location
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.ljb.data.mapper.mapperToCluster
import com.ljb.data.mapper.mapperToLatLng
import com.ljb.data.model.PolygonData
import com.ljb.data.model.SelectiveCluster
import com.ljb.domain.NetworkState
import com.ljb.domain.UiState
import com.ljb.domain.usecase.GetDbClinicUseCase
import com.ljb.domain.usecase.GetMapsPolygonUseCase
import com.ljb.domain.usecase.GetSelectiveClinicUseCase
import com.ljb.domain.usecase.GetTemporaryClinicUseCase
import com.ljb.domain.usecase.InsertSelectiveClinicUseCase
import com.project.kovid.widget.extension.MyLocationManager
import com.project.kovid.widget.extension.customview.HospClusterMarker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MapsViewModel @Inject constructor(
    private val getMapsPolygonUseCase: GetMapsPolygonUseCase,
    private val getSelectiveClinicUseCase: GetSelectiveClinicUseCase,
    private val getTemporaryClinicUseCase: GetTemporaryClinicUseCase,
    private val locationManager: MyLocationManager,

    private val getDbClinicUseCase: GetDbClinicUseCase,
    private val insertClinicUseCase: InsertSelectiveClinicUseCase
) : ViewModel() {
    private val tag = MapsViewModel::class.java.simpleName

    var progressState = MutableStateFlow(false)
    var detailAddress = Pair("", "")

    //선별 진료소
    private val _selectiveClusters = MutableStateFlow<UiState<List<SelectiveCluster>>>(UiState.Loading)
    val selectiveClusters: StateFlow<UiState<List<SelectiveCluster>>> get() = _selectiveClusters

    //임시 선별 진료소
    private val _temporaryClusters = MutableStateFlow<UiState<List<SelectiveCluster>>>(UiState.Loading)
    val temporaryClusters: StateFlow<UiState<List<SelectiveCluster>>> get() = _temporaryClusters


    //검색한 시도 지역의 영역 Polygon, centerPosition
    private val _polygonData = MutableSharedFlow<PolygonData>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val polygonData = _polygonData.asSharedFlow()

    //현위치
    private val _currentLocation = MutableSharedFlow<Location>(
        replay = 0,                         //새로운 구독자에게 이전 이벤트를 전달하지 않음
        extraBufferCapacity = 1,            //추가 버퍼를 생성하여 emit 한 데이터가 버퍼에 유지되도록 함
        onBufferOverflow = BufferOverflow.DROP_OLDEST   //버퍼가 가득찼을 시 오래된 데이터 제거
    )
    val currentLocation = _currentLocation.asSharedFlow()

    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            locationResult.lastLocation?.let {
                viewModelScope.launch {
                    val sido = locationManager.getReverseGeocoding(it)
                    detailAddress = sido
                    _currentLocation.emit(it)

                    getDbData(sido.first, sido.second)
                }
            }
        }
    }

    fun startLocation() = locationManager.startLocationUpdates(mLocationCallback)
    fun stopLocation() = locationManager.stopLocationUpdates(mLocationCallback)

    fun getDbData(siDo: String, siGunGu: String) {
        viewModelScope.launch {

            withContext(Dispatchers.IO) {
                detailAddress = Pair(siDo, siGunGu)
                progressState.emit(true)

                with(getDbClinicUseCase(siDo, siGunGu, HospClusterMarker.HOSP_SELECTIVE)) {
                    if (isEmpty())
                        getSelectiveData(siDo, siGunGu) //DB Data 없을시 Remote API 호출
                    else {
                        _selectiveClusters.emit(
                            UiState.Complete(map {
                                it.mapperToCluster(locationManager.getGeocoding(it.addr))
                            })
                        )
                    }
                }

                with(getDbClinicUseCase(siDo, siGunGu, HospClusterMarker.HOSP_TEMPORARY)) {
                    if (isEmpty())
                        getTemporaryData(siDo, siGunGu)
                    else {
                        _temporaryClusters.emit(
                            UiState.Complete(map {
                                it.mapperToCluster(locationManager.getGeocoding(it.addr))
                            })
                        )
                    }
                }

                getMapsPolygonUseCase(siDo, siGunGu).catch { exception ->
                    Log.d(tag, "getMapsPolygonUseCase Exception Error: ${exception.message}")
                }.collect { result ->
                    when (result) {
                        is NetworkState.Success -> {
                            _polygonData.emit(result.data.mapperToLatLng())
                        }
                        is NetworkState.Error -> {}
                        is NetworkState.Loading -> {}
                    }
                }

                progressState.emit(false)
            }
        }
    }

    private suspend fun getSelectiveData(siDo: String, siGunGu: String) {
        getSelectiveClinicUseCase(siDo, siGunGu).catch { exception ->
            Log.d(tag, "getSelectiveData Exception Error: ${exception.message}")
        }.collect { result ->
            when (result) {
                is NetworkState.Success -> {
                    Log.d(tag, "getSelectiveData Success: Total${result.data.size} ${result.data}")

                    //Location 값이 정상 반환일 때를 위한 필터링
                    result.data.filter {
                        with(locationManager.getGeocoding(it.addr)) {
                            latitude != 0.0 && longitude != 0.0
                        }
                    }.run {
                        forEach {
                            insertClinicUseCase(it, HospClusterMarker.HOSP_SELECTIVE) //이후 DB 저장
                        }

                        val cluster = getDbClinicUseCase(siDo, siGunGu, HospClusterMarker.HOSP_SELECTIVE).map {
                            it.mapperToCluster(locationManager.getGeocoding(it.addr))
                        }
                        _selectiveClusters.emit(UiState.Complete(cluster))
                    }
                }
                is NetworkState.Error -> {
                    _selectiveClusters.emit(UiState.Fail(result.message))
                    Log.d(tag, "getSelectiveData Error : ${result.message}")
                }
                is NetworkState.Loading -> _selectiveClusters.emit(UiState.Loading)
            }
        }
    }

    private suspend fun getTemporaryData(siDo: String, siGunGu: String) {
        getTemporaryClinicUseCase(siDo, siGunGu).catch { exception ->
            Log.d(tag, "getTemporaryData Exception Error: ${exception.message}")
        }.collect{ result ->
            when (result) {
                is NetworkState.Success -> {
                    Log.d(tag, "getTemporaryData Success: Total${result.data.size} ${result.data}")

                    //Location 값이 정상 반환일 때를 위한 필터링
                    result.data.filter {
                        with(locationManager.getGeocoding(it.addr)) { latitude != 0.0 && longitude != 0.0 }
                    }.run {
                        forEach {
                            insertClinicUseCase(it, HospClusterMarker.HOSP_TEMPORARY)
                        }

                        val cluster = getDbClinicUseCase(siDo, siGunGu, HospClusterMarker.HOSP_TEMPORARY).map {
                            it.mapperToCluster(locationManager.getGeocoding(it.addr))
                        }
                        _temporaryClusters.emit(UiState.Complete(cluster))
                    }
                }
                is NetworkState.Error -> {
                    _temporaryClusters.emit(UiState.Fail(result.message))
                    Log.d(tag, "getTemporaryData Error : ${result.message}")
                }
                is NetworkState.Loading -> _temporaryClusters.emit(UiState.Loading)
            }
        }
    }
}