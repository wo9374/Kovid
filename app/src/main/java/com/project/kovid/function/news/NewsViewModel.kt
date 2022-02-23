package com.project.kovid.function.news

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.project.kovid.model.NewsData
import com.project.kovid.repository.NewsRepository
import com.project.kovid.util.TimeUtil
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class NewsViewModel(application: Application) : AndroidViewModel(application) {

    private val newsRepo: NewsRepository = NewsRepository()

    var newsData = MutableLiveData<List<NewsData.Article>>()

    lateinit var newsDetailData : NewsData.Article

    fun searchNewsApi(){
        viewModelScope.launch {
            //TODO try/catch 삭제유무 고민
            try {
                val result = newsRepo.getNewsData(TimeUtil.getPast1Month())

                if (result.isSuccessful && result.body() != null){
                    newsData.postValue(result.body()?.articles!!)
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