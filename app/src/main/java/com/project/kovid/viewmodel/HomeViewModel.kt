package com.project.kovid.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ljb.domain.NetworkState
import com.ljb.domain.UiState
import com.ljb.domain.entity.AreaCovid
import com.ljb.domain.entity.WeekCovid
import com.ljb.domain.usecase.GetAreaListUseCase
import com.ljb.domain.usecase.GetChartListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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


    private lateinit var monthDecide: List<WeekCovid>
    private lateinit var weekDecide: List<WeekCovid>

    //7일, 30일 교체하면서 보여줄 StateFlow
    private val _covidList = MutableStateFlow<UiState<List<WeekCovid>>>(UiState.Loading)
    val covidList : StateFlow<UiState<List<WeekCovid>>> get() = _covidList

    //지역별 확진자
    private val _areaList = MutableStateFlow<UiState<List<AreaCovid>>>(UiState.Loading)
    val areaList : StateFlow<UiState<List<AreaCovid>>> get() = _areaList

    init {
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                getChartListUseCase()
                    .onStart {} //로딩시작때
                    .catch { exception ->
                        Log.d(tag, "CovidChart - Exception Error : ${exception.message}")
                    }
                    .collect{ result ->
                        when(result){
                            is NetworkState.Success -> {
                                monthDecide = result.data

                                val weekCovid = arrayListOf<WeekCovid>()
                                for (i in monthDecide.size - 7 until monthDecide.size) {
                                    weekCovid.add(monthDecide[i])
                                }
                                weekDecide = weekCovid
                                _covidList.emit(UiState.Complete(weekDecide))
                                Log.d(tag, "CovidList - Network Success : ${result.data}")
                            }
                            is NetworkState.Error -> {
                                _covidList.emit(UiState.Fail(result.message))
                                Log.d(tag, "CovidList - Network Error : ${result.message}")
                            }
                            is NetworkState.Loading -> {
                                _covidList.emit(UiState.Loading)
                            }
                        }
                    }

                getAreaListUseCase()
                    .catch { exception ->
                        Log.d(tag, "CovidArea - Exception Error : ${exception.message}")
                        _areaList.emit(UiState.Fail("서버 오류"))
                    }.collectLatest { result ->
                        when(result){
                            is NetworkState.Success -> {
                                _areaList.emit(UiState.Complete(result.data))
                                Log.d(tag, "CovidArea - Network Success : ${result.data}")
                            }
                            is NetworkState.Error -> {
                                _areaList.emit(UiState.Fail(result.message))
                                Log.d(tag, "CovidArea - Network Error : ${result.message}")
                            }
                            else -> {

                            }
                        }
                    }
            }
        }
    }

    fun weekDataSet(){ _covidList.value = UiState.Complete(weekDecide) }
    fun monthDataSet() { _covidList.value = UiState.Complete(monthDecide) }
}