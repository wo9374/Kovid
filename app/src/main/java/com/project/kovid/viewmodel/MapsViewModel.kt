package com.project.kovid.viewmodel

import android.location.Location
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.PolyUtil
import com.ljb.data.mapper.mapperToCluster
import com.ljb.data.model.ClinicCluster
import com.ljb.data.model.PolygonData.Companion.MULTI_POLYGON
import com.ljb.data.model.PolygonData.Companion.POLYGON
import com.ljb.domain.NetworkState
import com.ljb.domain.UiState
import com.ljb.domain.entity.Clinic
import com.ljb.domain.entity.SiDo
import com.ljb.domain.usecase.GetDbClinicUseCase
import com.ljb.domain.usecase.GetRemoteClinicUseCase
import com.ljb.domain.usecase.InsertSelectiveClinicUseCase
import com.ljb.domain.usecase.MapJsonParsingUseCase
import com.project.kovid.di.MyApplication
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
    private val locationManager: MyLocationManager,
    //private val getMapsPolygonUseCase: GetMapsPolygonUseCase,

    private val getRemoteClinicUseCase: GetRemoteClinicUseCase,
    private val getDbClinicUseCase: GetDbClinicUseCase,
    private val insertClinicUseCase: InsertSelectiveClinicUseCase,
    private val mapJsonParsingUseCase: MapJsonParsingUseCase,
) : ViewModel() {
    private val tag = MapsViewModel::class.java.simpleName

    private val settingGlobalDataInit = "mapsFragment_dataInit"

    var progressState = MutableStateFlow(false)
    var detailAddress = Pair("", "")

    //선별 진료소
    private val _selectiveClusters = MutableStateFlow<UiState<List<ClinicCluster>>>(UiState.Loading)
    val selectiveClusters: StateFlow<UiState<List<ClinicCluster>>> get() = _selectiveClusters

    //임시 선별 진료소
    private val _temporaryClusters = MutableStateFlow<UiState<List<ClinicCluster>>>(UiState.Loading)
    val temporaryClusters get() = _temporaryClusters

    var mapJson = Pair("","")
    //맵 폴리곤
    private var _mapsPolygon = MutableStateFlow<List<SiDo>>(emptyList())
    val mapsPolygon get() = _mapsPolygon
    //private val _mapsPolygon = MutableSharedFlow<List<SiDo>>()
    //val mapsPolygon = _mapsPolygon.asSharedFlow()

    private val _currentPolygon = MutableSharedFlow<Pair<String, List<List<LatLng>>>>()
    val currentPolygon = _currentPolygon.asSharedFlow()

    //검색한 시도 지역의 영역 Polygon, centerPosition
    /*private val _polygonData = MutableSharedFlow<PolygonData>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val polygonData = _polygonData.asSharedFlow()*/

    //현위치
    private val _currentLocation = MutableSharedFlow<Location>(
        replay = 0,                         //새로운 구독자에게 이전 이벤트를 전달하지 않음
        extraBufferCapacity = 1,            //추가 버퍼를 생성하여 emit 한 데이터가 버퍼에 유지 되도록 함
        onBufferOverflow = BufferOverflow.DROP_OLDEST   //버퍼가 가득 찼을시 오래된 데이터 제거
    )
    val currentLocation = _currentLocation.asSharedFlow()

    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            locationResult.lastLocation?.let {
                viewModelScope.launch {
                    detailAddress = locationManager.getReverseGeocoding(it)
                    _currentLocation.emit(it)
                }
            }
        }
    }

    fun startLocation() = locationManager.startLocationUpdates(mLocationCallback)
    fun stopLocation() = locationManager.stopLocationUpdates(mLocationCallback)

    //병원 정보를 가지고 있는지 check
    fun checkInitialData() : Boolean = MyApplication.preferences.getBoolean(settingGlobalDataInit, false)

    fun parsingMapJson(jsonSiDo: String, jsonSiGunGu: String){
        mapJson = Pair(jsonSiDo, jsonSiGunGu)
    }

    fun isInPolygon(cameraPosition: LatLng) {
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                if (mapsPolygon.value.isNotEmpty()){
                    mapsPolygon.value.forEach { siDo ->
                        when(siDo.polygonType){
                            POLYGON -> {
                                val boundary = siDo.polygon[0].map { LatLng(it[0], it[1]) }
                                if (PolyUtil.containsLocation(cameraPosition, boundary, true))
                                    _currentPolygon.emit(Pair(siDo.polygonType, listOf(boundary)))
                            }
                            MULTI_POLYGON -> {
                                val boundary = siDo.polygon.map { multi -> multi.map { LatLng(it[0], it[1]) } }
                                boundary.forEach {
                                    if (PolyUtil.containsLocation(cameraPosition, it, true))
                                        _currentPolygon.emit(Pair(siDo.polygonType, boundary))
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    fun getInitialRemoteData(){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                mapJsonParsingUseCase(mapJson.first, mapJson.second).collectLatest {
                    _mapsPolygon.emit(it)
                }

                getRemoteClinicUseCase(Clinic.CLINIC_SELECTIVE).catch { exception ->
                    Log.d(tag, "getInitialRemoteData Selective Exception : ${exception.message}")
                }.collectLatest { result ->
                    when(result){
                        is NetworkState.Success -> {
                            //Location 값이 정상 반환일 때를 위한 필터링
                            result.data.filter {
                                with(locationManager.getGeocoding(it.addr)) { latitude != 0.0 && longitude != 0.0 }
                            }.forEach {
                                insertClinicUseCase(it, Clinic.CLINIC_SELECTIVE) //이후 DB 저장
                            }
                        }
                        is NetworkState.Error -> Log.d(tag, "getSelectiveClinicUseCase Error : ${result.message}")
                        is NetworkState.Loading -> {}
                    }
                }

                getRemoteClinicUseCase(Clinic.CLINIC_TEMPORARY).catch { exception ->
                    Log.d(tag, "getInitialRemoteData Temporary Exception : ${exception.message}")
                }.collectLatest { result ->
                    when(result){
                        is NetworkState.Success -> {
                            result.data.filter {
                                with(locationManager.getGeocoding(it.addr)) { latitude != 0.0 && longitude != 0.0 }
                            }.forEach {
                                insertClinicUseCase(it, Clinic.CLINIC_TEMPORARY)
                            }
                        }
                        is NetworkState.Error -> Log.d(tag, "getTemporaryClinicUseCase Error : ${result.message}")
                        is NetworkState.Loading -> {}
                    }
                }


                MyApplication.preferences.setBoolean(settingGlobalDataInit, true)
                getDbDataLoading(detailAddress.first, detailAddress.second)
            }
        }
    }

    fun getDbDataLoading(siDo: String, siGunGu: String, progressEnabled: Boolean = false){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                mapJsonParsingUseCase(mapJson.first, mapJson.second).collectLatest {
                    _mapsPolygon.emit(it)
                }

                if (progressEnabled)
                    progressState.emit(true)

                detailAddress = Pair(siDo, siGunGu)

                getDbClinicUseCase(siDo, siGunGu, Clinic.CLINIC_SELECTIVE).let { list ->
                    if (list.isEmpty())
                        _selectiveClusters.emit(UiState.Fail("선별 진료소 정보가 없습니다."))
                    else{
                        val cluster = list.map {
                            it.mapperToCluster(locationManager.getGeocoding(it.addr))
                        }
                        _selectiveClusters.emit(UiState.Complete(cluster))
                    }
                }

                getDbClinicUseCase(siDo, siGunGu, Clinic.CLINIC_TEMPORARY).let { list ->
                    if (list.isEmpty())
                        _temporaryClusters.emit(UiState.Fail("임시 선별 진료소 정보가 없습니다."))
                    else{
                        val cluster = list.map {
                            it.mapperToCluster(locationManager.getGeocoding(it.addr))
                        }
                        _temporaryClusters.emit(UiState.Complete(cluster))
                    }
                }

                /*getMapsPolygonUseCase(siDo, siGunGu).catch { exception ->
                    Log.d(tag, "getMapsPolygonUseCase Exception Error: ${exception.message}")
                }.collect { result ->
                    when (result) {
                        is NetworkState.Success -> _polygonData.emit(result.data.mapperToLatLng())
                        is NetworkState.Error -> {}
                        is NetworkState.Loading -> {}
                    }
                }*/
                progressState.emit(false)
            }
        }
    }
}