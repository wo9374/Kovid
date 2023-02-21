package com.project.kovid.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.ljb.data.util.getPast1MonthCovid
import com.project.kovid.model.Article
import com.project.kovid.function.repository.NewsRepository
import com.project.kovid.model.NaverNews
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class NewsViewModel(application: Application) : AndroidViewModel(application) {

    private val newsRepo: NewsRepository = NewsRepository()

    var newsData = MutableLiveData<List<Article>>()
    lateinit var newsDetailData : Article

    var naverData = MutableLiveData<NaverNews>()

    fun searchNewsApi(){
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val result = newsRepo.getNewsData(Date().getPast1MonthCovid())

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
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val result = newsRepo.getNaverNewsData()

                if (result.isSuccessful && result.body() != null){
                    naverData.postValue(result.body())
                }else{
                    Log.d("NewsViewModel", "searchNaverNews() result not Successful or result.body null")
                }

            } catch (e: Exception) {
                Log.d("NewsViewModel", "searchNaverNews() fail...")
            }
        }
    }
}