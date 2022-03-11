package com.project.kovid.function.home

import android.app.Application
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.lifecycle.*
import com.project.kovid.R
import com.project.kovid.function.repository.CovidRepository
import com.project.kovid.model.WeekCovid
import com.project.kovid.util.StringUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel(application: Application) : AndroidViewModel(application) {
    private val TAG = HomeViewModel::class.java.simpleName

    private val covidRepo: CovidRepository = CovidRepository()

    val topDecideDate = MutableLiveData<String>()
    val topDecide = MutableLiveData<String>()

    //var uiModeColor = MutableLiveData(ContextCompat.getColor(application, R.color.black))


    var resultDecide = ArrayList<WeekCovid>()
    val currentDecide = MutableLiveData<ArrayList<WeekCovid>>()

    fun getWeekCovid(){
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val apiResultData = covidRepo.getCovidWeek()

                if (apiResultData.isSuccessful && apiResultData.body() != null) {
                    var resultData = apiResultData.body()!!.body.items.item
                    resultData = resultData.sortedBy{ it.stateDt }

                    val computeList = arrayListOf<WeekCovid>()
                    for (i in 0 .. 30){ //한달
                        val currentDate = StringUtil.computeStringToInt(resultData[i].stateDt)
                        val decideCnt = (resultData[i+1].decideCnt - resultData[i].decideCnt)
                        computeList.add(WeekCovid(currentDate,decideCnt))
                    }
                    resultDecide = computeList

                    //초기 일주일 set
                    weekDataSet()

                    topDecideDate.postValue(computeList[computeList.lastIndex].day)
                    topDecide.postValue(StringUtil.getDecimalFormatNum(computeList[computeList.lastIndex].decideCnt))
                } else {
                    Log.d(TAG, "getWeekCovid() result not Successful or result.body null")
                }
            } catch (e: Exception) {
                Log.d(TAG, "getWeekCovid() fail...")
            }
        }
    }

    fun weekDataSet(){
        val weekCovid = arrayListOf<WeekCovid>()
        for (i in resultDecide.size - 7 until resultDecide.size){weekCovid.add(resultDecide[i])}
        currentDecide.postValue(weekCovid)
    }

    fun monthDataSet(){
        currentDecide.postValue(resultDecide)
    }
}