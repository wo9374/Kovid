package com.project.kovid.function.news

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.project.kovid.model.News
import kotlinx.coroutines.launch

class NewsViewModel(application: Application) : AndroidViewModel(application) {

    private val newsRepo: NewsRepository = NewsRepository()

    var newsDataList = MutableLiveData<News>()

    fun searchNewsApi(){
        viewModelScope.launch {
            try {
                val result = newsRepo.getData()
                if (result.isSuccessful && result.body() != null){
                    newsDataList.postValue(result.body())
                }else{
                    Log.d("NewsViewModel", "searchNewsApi() result not Successful or result.body null")
                }
            }catch (e:Exception){
                Log.d("NewsViewModel", "searchNewsApi() fail...")
            }
        }
    }

    //네이버 검색
    fun searchNaverNews(){
        viewModelScope.launch {
            try {
                val result = newsRepo.getNaverNewsData()
            } catch (e: Exception) {
                Log.d("NewsViewModel", "searchNews() fail...")
            }
        }
    }

}