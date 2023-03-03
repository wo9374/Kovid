package com.project.kovid.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ljb.data.mapper.mapperToLatLng
import com.ljb.data.model.PolygonData
import com.ljb.data.util.getPast1MonthCovid
import com.ljb.domain.NetworkState
import com.ljb.domain.UiState
import com.ljb.domain.entity.News
import com.ljb.domain.usecase.GetNewUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.URLEncoder
import java.util.*
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val getNewUseCase: GetNewUseCase,
) : ViewModel() {
    private val tag = NewsViewModel::class.java.simpleName

    private val _naverNews = MutableStateFlow<List<News>>(emptyList())
    val naverNews get() = _naverNews

    private val _newsDetail = MutableStateFlow(News("","","","","","",""))
    val newsDetail get() = _newsDetail

    init {
        getNews()
    }

    private fun getNews(){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                getNewUseCase(URLEncoder.encode("Covid","UTF-8")).catch { exception->
                    Log.d(tag, "getNewUseCase Exception Error: ${exception.message}")
                }.collect{ result ->
                    when (result) {
                        is NetworkState.Success -> {
                            Log.d(tag, "getNewUseCase Success: Total${result.data.size} ${result.data}")
                            _naverNews.emit(result.data)
                        }
                        is NetworkState.Error -> {}
                        is NetworkState.Loading -> {}
                    }
                }
            }
        }
    }

    fun setNewsDetail(news: News){
        viewModelScope.launch {
            _newsDetail.emit(news)
        }
    }
}