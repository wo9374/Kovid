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
import com.ljb.domain.usecase.GetDbSelectiveClinicUseCase
import com.ljb.domain.usecase.GetMapsPolygonUseCase
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
    private val getMapsPolygonUseCase: GetMapsPolygonUseCase,
    private val getSelectiveClinicUseCase: GetSelectiveClinicUseCase,
    private val locationManager: MyLocationManager,

    private val getDbSelectiveClinicUseCase: GetDbSelectiveClinicUseCase,
    private val insertSelectiveClinicUseCase: InsertSelectiveClinicUseCase
) : ViewModel() {
    private val tag = MapsViewModel::class.java.simpleName

    private val _hospitalClusters = MutableStateFlow<UiState<List<SelectiveCluster>>>(UiState.Loading)
    val hospitalClusters: StateFlow<UiState<List<SelectiveCluster>>> get() = _hospitalClusters


    /** 검색한 시도 지역의 영역 Polygon, centerPosition */
    private val _polygonData = MutableSharedFlow<PolygonData>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val polygonData = _polygonData.asSharedFlow()

    /**
     *현위치 get
     * */
    private val _currentLocation = MutableSharedFlow<Location>(
        replay = 0,                         //새로운 구독자에게 이전 이벤트를 전달하지 않음
        extraBufferCapacity = 1,            //추가 버퍼를 생성하여 emit 한 데이터가 버퍼에 유지되도록 함
        onBufferOverflow = BufferOverflow.DROP_OLDEST   //버퍼가 가득찼을 시 오래된 데이터 제거
    )
    val currentLocation = _currentLocation.asSharedFlow()


    private val _detailAddress = MutableStateFlow(Pair("",""))
    val detailAddress get() = _detailAddress

    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            locationResult.lastLocation?.let {

                viewModelScope.launch {
                    val sido = locationManager.getReverseGeocoding(it)
                    _detailAddress.emit(sido)
                    _currentLocation.emit(it)


                    detailAddress.collectLatest {
                        if (it.first.isNotEmpty()){
                            getMapsPolyGon(it.first, it.second)
                            getDbData(it.first, it.second)
                        }
                    }
                }

            }

        }
    }

    fun startLocation() = locationManager.startLocationUpdates(mLocationCallback)
    fun stopLocation() = locationManager.stopLocationUpdates(mLocationCallback)

    fun getMapsPolyGon(addr:String, sigungu: String = ""){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                getMapsPolygonUseCase(addr, sigungu).catch { exception ->
                    Log.d(tag, "getMapsPolygonUseCase Exception Error: ${exception.message}")
                }.collect { result ->
                    when(result){
                        is NetworkState.Success -> {
                            _polygonData.emit(result.data.mapperToLatLng())
                        }
                        is NetworkState.Error -> {}
                        is NetworkState.Loading -> {}
                    }
                }
            }
        }
    }

    fun getDbData(sido: String, sigungu: String = "") {
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                getDbSelectiveClinicUseCase(sido, sigungu).apply {
                    if (isEmpty()) {
                        //DB Data 없을시 Remote API 호출
                        getRemoteData(sido)
                    } else {
                        _hospitalClusters.emit(
                            UiState.Complete(
                                map {
                                    it.mapperToCluster(locationManager.getGeocoding(it.addr))
                                }
                            )
                        )
                    }

                }
            }
        }
    }

    fun getRemoteData(siDo: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {

                getSelectiveClinicUseCase(siDo)
                    .catch { exception ->
                        Log.d(tag, "SelectiveClinic Exception Error: ${exception.message}")
                    }.collect { result ->
                        when (result) {
                            is NetworkState.Success -> {
                                //Location 값이 정상 반환일때를 위한 필터링
                                val filteringAddress = result.data.filter {
                                    with(locationManager.getGeocoding(it.addr)){
                                        latitude != 0.0 && longitude != 0.0
                                    }
                                }

                                _hospitalClusters.emit(
                                    UiState.Complete(filteringAddress.map {
                                        it.mapperToCluster(locationManager.getGeocoding(it.addr))
                                    })
                                )
                                Log.d(tag, "SelectiveClinic Success: Total${result.data.size} ${result.data}")

                                //이후 DB 저장
                                filteringAddress.forEach {
                                    insertSelectiveClinicUseCase(it)
                                }
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