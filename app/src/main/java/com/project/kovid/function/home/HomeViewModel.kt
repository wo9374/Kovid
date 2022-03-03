package com.project.kovid.function.home

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.project.kovid.function.repository.CovidRepository
import com.project.kovid.model.CovidItem
import com.project.kovid.model.WeekCovid
import com.project.kovid.util.StringUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel(application: Application) : AndroidViewModel(application) {
    private val TAG = HomeViewModel::class.java.simpleName

    private val covidRepo: CovidRepository = CovidRepository()

    val weekDecide = MutableLiveData<ArrayList<WeekCovid>>()

    fun getWeekCovid(){
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val apiResultData = covidRepo.getCovidWeek()

                if (apiResultData.isSuccessful && apiResultData.body() != null) {
                    val resultData = apiResultData.body()!!.body.items.item
                    val weekCovid = arrayListOf<WeekCovid>()

                    for (i in 6 downTo 0){ //일주일
                        val currentDate = StringUtil.computeStringToInt(resultData[i+1].stateDt)
                        val decideCnt = (resultData[i].decideCnt - resultData[i+1].decideCnt)
                        weekCovid.add(WeekCovid(currentDate,decideCnt))
                    }
                    weekDecide.postValue(weekCovid)
                } else {
                    Log.d(TAG, "getCovidItem() result not Successful or result.body null")
                }
            } catch (e: Exception) {
                Log.d("MainViewModel", "fail...")
            }
        }
    }
}