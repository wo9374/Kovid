package com.project.kovid.function.news

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.project.kovid.model.NewsData
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class NewsViewModel(application: Application) : AndroidViewModel(application) {

    private val newsRepo: NewsRepository = NewsRepository()

    var newsData = MutableLiveData<List<NewsData.Article>>()

    fun searchNewsApi(){
        viewModelScope.launch {
            //TODO try/catch 삭제유무 고민
            try {
                val result = newsRepo.getNewsData(getPastDate())

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

    private fun getPastDate() : String{
        //오늘
        val today = Date()
        val date = SimpleDateFormat("yyyy-MM-dd")
        val toDay: String = date.format(today)

        // 1시간전
        val cal: Calendar = Calendar.getInstance()
        cal.setTime(today)
        cal.add(Calendar.HOUR, -1)

        // 포맷변경 ( 년월일 시분초)
        val sdformat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")
        sdformat.setTimeZone(TimeZone.getTimeZone("UTC"))

        val beforeHour: String = sdformat.format(cal.time)

        //하루 전
        val day: Calendar = Calendar.getInstance()
        day.add(Calendar.DATE, -1)
        val beforeDate = SimpleDateFormat("yyyy-MM-dd").format(day.time)

        //한주 전
        val week: Calendar = Calendar.getInstance()
        week.add(Calendar.DATE, -7)
        val beforeWeek = SimpleDateFormat("yyyy-MM-dd").format(week.time)

        //한달 전
        val mon: Calendar = Calendar.getInstance()
        mon.add(Calendar.MONTH, -1)
        val beforeMonth = SimpleDateFormat("yyyy-MM-dd").format(mon.time)

        return beforeMonth
    }
}