package com.project.kovid.function.news

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.project.kovid.model.KovidNews
import kotlinx.coroutines.launch

class NewsViewModel(application: Application) : AndroidViewModel(application) {

    private val newsRepo: NewsRepository = NewsRepository()

    private var newsDataList = MutableLiveData<KovidNews>()

    fun searchNews(){
        viewModelScope.launch {
            try {
                val result = newsRepo.getNaverNewsData()
            } catch (e: Exception) {
                Log.d("NewsViewModel", "searchNews() fail...")
            }
        }
    }
}