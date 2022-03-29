package com.project.kovid.function.news

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.project.kovid.model.Article
import com.project.kovid.function.repository.NewsRepository
import com.project.kovid.util.TimeUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NewsViewModel(application: Application) : AndroidViewModel(application) {

    private val newsRepo: NewsRepository = NewsRepository()

    var newsData = MutableLiveData<List<Article>>()

    lateinit var newsDetailData : Article

    fun searchNewsApi(){
        CoroutineScope(Dispatchers.IO).launch {
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
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val result = newsRepo.getNaverNewsData()

            } catch (e: Exception) {
                Log.d("NewsViewModel", "searchNaverNews() fail...")
            }
        }
    }
}