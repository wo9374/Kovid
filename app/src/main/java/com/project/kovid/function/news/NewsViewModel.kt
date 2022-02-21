package com.project.kovid.function.news

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.project.kovid.model.NaverNews
import com.project.kovid.model.News
import kotlinx.coroutines.launch

class NewsViewModel(application: Application) : AndroidViewModel(application) {

    private val newsRepo: NewsRepository = NewsRepository()

    private var newsDataList = MutableLiveData<List<News>>()

    fun searchNaverNews(){
        viewModelScope.launch {
            try {
                val result = newsRepo.getNaverNewsData()
            } catch (e: Exception) {
                Log.d("NewsViewModel", "searchNews() fail...")
            }
        }
    }

    fun searchNewsApi(){
        viewModelScope.launch {
            try {
                val result = newsRepo.getData()
                Log.d("NewsViewModel", "searchNewsApi() Body ${result}")
            }catch (e:Exception){
                Log.d("NewsViewModel", "searchNewsApi() fail...")
            }
        }
    }
}