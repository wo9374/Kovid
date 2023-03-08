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
import com.ljb.data.model.ClinicCluster
import com.ljb.domain.NetworkState
import com.ljb.domain.UiState
import com.ljb.domain.entity.Clinic
import com.ljb.domain.usecase.*
import com.project.kovid.di.MyApplication
import com.project.kovid.widget.extension.MyLocationManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MapsViewModel @Inject constructor(
    private val locationManager: MyLocationManager,
    private val getMapsPolygonUseCase: GetMapsPolygonUseCase,

    private val getRemoteClinicUseCase: GetRemoteClinicUseCase,
    private val getDbClinicUseCase: GetDbClinicUseCase,
    private val insertClinicUseCase: InsertSelectiveClinicUseCase
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
    val temporaryClusters: StateFlow<UiState<List<ClinicCluster>>> get() = _temporaryClusters


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
        extraBufferCapacity = 1,            //추가 버퍼를 생성하여 emit 한 데이터가 버퍼에 유지 되도록 함
        onBufferOverflow = BufferOverflow.DROP_OLDEST   //버퍼가 가득 찼을시 오래된 데이터 제거
    )
    val currentLocation = _currentLocation.asSharedFlow()

    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            locationResult.lastLocation?.let {
                viewModelScope.launch {
                    val currentRegion = locationManager.getReverseGeocoding(it)
                    detailAddress = currentRegion
                    _currentLocation.emit(it)
                }
            }
        }
    }

    fun startLocation() = locationManager.startLocationUpdates(mLocationCallback)
    fun stopLocation() = locationManager.stopLocationUpdates(mLocationCallback)

    /**
     * 데이터가 초기화 됐는지 확인하고 진료소 데이터를 가져온다.
     */
    fun loadClinicData() {
        if (isDataInitialized()) {
            getClinicDataFromDatabase(
                siDo = detailAddress.first,
                siGunGu = detailAddress.second,
            )
        } else {
            fetchClinicDataFromRemote()
        }
    }

    /**
     * 데이터 초기화 여부 확인하지 않고 특정 지역의 진료소 데이터를 가져온다.
     */
    fun loadClinicData(siDo: String, siGunGu: String) {
        getClinicDataFromDatabase(
            siDo = siDo,
            siGunGu = siGunGu,
            progressEnabled = true
        )
    }

    //병원 정보를 가지고 있는지 check
    fun isDataInitialized() : Boolean = MyApplication.preferences.getBoolean(settingGlobalDataInit, false)

    /**
     * Remote에서 진료소 데이터를 가져와 Database에 저장한 후 [getClinicDataFromDatabase]를 호출한다.
     */
    private fun fetchClinicDataFromRemote() {
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                getRemoteClinicUseCase(Clinic.CLINIC_SELECTIVE)
                    .catch { exception ->
                        Log.d(tag, "getInitialRemoteData Selective Exception : ${exception.message}")
                    }
                    .collectLatest { result ->
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

                getRemoteClinicUseCase(Clinic.CLINIC_TEMPORARY)
                    .catch { exception ->
                        Log.d(tag, "getInitialRemoteData Temporary Exception : ${exception.message}")
                    }
                    .collectLatest { result ->
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
                getClinicDataFromDatabase(detailAddress.first, detailAddress.second)
            }
        }
    }

    /**
     * Database에 저장된 진료소 데이터를 읽어온다.
     */
    private fun getClinicDataFromDatabase(
        siDo: String,
        siGunGu: String,
        progressEnabled: Boolean = false
    ) {
        viewModelScope.launch {
            withContext(Dispatchers.IO){
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

                getMapsPolygonUseCase(siDo, siGunGu).catch { exception ->
                    Log.d(tag, "getMapsPolygonUseCase Exception Error: ${exception.message}")
                }.collect { result ->
                    when (result) {
                        is NetworkState.Success -> _polygonData.emit(result.data.mapperToLatLng())
                        is NetworkState.Error -> {}
                        is NetworkState.Loading -> {}
                    }
                }
                progressState.emit(false)
            }
        }
    }
}