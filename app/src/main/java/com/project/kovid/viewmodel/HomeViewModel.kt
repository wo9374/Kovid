package com.project.kovid.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ljb.domain.NetworkState
import com.ljb.domain.UiState
import com.ljb.domain.entity.Area
import com.ljb.domain.entity.WeekCovid
import com.ljb.domain.usecase.GetAreaListUseCase
import com.ljb.domain.usecase.GetChartListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * UseCase를 Hilt를 이용해 주입받아 사용
 * */
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getChartListUseCase: GetChartListUseCase,
    private val getAreaListUseCase: GetAreaListUseCase
) : ViewModel() {
    val tag: String = this::class.java.simpleName

    private lateinit var resultDecide: List<WeekCovid>

    //Covid Chart UI 상태 체크
    val uiState = MutableStateFlow<UiState>(UiState.Loading)

    //7일, 30일 교체하면서 보여줄 StateFlow
    private val _covidList = MutableStateFlow<List<WeekCovid>>(mutableListOf())
    val covidList : StateFlow<List<WeekCovid>> get() = _covidList

    //지역별 확진자
    private val _areaList = MutableStateFlow<List<Area>>(mutableListOf())
    val areaList : StateFlow<List<Area>> get() = _areaList

    init {
        viewModelScope.launch {
            getChartListUseCase()
                .onStart {} //로딩시작때
                .catch { exception ->
                    Log.d(tag, "CovidChart - Exception Error : ${exception.message}")
                }
                .collectLatest { result ->
                    when(result){
                        is NetworkState.Success -> {
                            resultDecide = result.data
                            Log.d(tag, "CovidList - Network Success : ${result.data}")
                            uiState.value = UiState.Complete
                        }
                        is NetworkState.Error -> {
                            Log.d(tag, "CovidList - Network Error : ${result.message}")
                            uiState.value = UiState.Fail
                        }
                        else -> {
                            uiState.value = UiState.Loading
                        }
                    }
                }

            getAreaListUseCase()
                .catch { exception ->
                    Log.d(tag, "CovidArea - Exception Error : ${exception.message}")
                }.collectLatest { result ->
                    when(result){
                        is NetworkState.Success -> {
                            _areaList.value = result.data
                            Log.d(tag, "CovidArea - Network Success : ${result.data}")
                        }
                        is NetworkState.Error -> {

                            Log.d(tag, "CovidArea - Network Error : ${result.message}")
                        }
                        else -> {

                        }
                    }
                }
        }
    }

    fun weekDataSet() {
        if (resultDecide.isNotEmpty()) {
            val weekCovid = arrayListOf<WeekCovid>()
            for (i in resultDecide.size - 7 until resultDecide.size) {
                weekCovid.add(resultDecide[i])
            }
            _covidList.value = weekCovid
        }
    }

    fun monthDataSet() {
        if (resultDecide.isNotEmpty())
            _covidList.value = resultDecide
    }
}