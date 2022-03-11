package com.project.kovid.function.home

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.project.kovid.function.repository.CovidRepository
import com.project.kovid.model.AreaData
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

    fun getChartData(){
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val apiResultData = covidRepo.getCovidChartData()
                if (apiResultData.isSuccessful && apiResultData.body() != null) {

                    var resultData = apiResultData.body()!!.chartBody.chartItems.chartItem
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
                    Log.d(TAG, "getChartData() Result not Successful or result.body null")
                }
            } catch (e: Exception) {
                Log.d(TAG, "getChartData() Fail...")
            }
        }
    }

    fun getAreaData(){
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val apiResultData = covidRepo.getCovidAreaData()
                if (apiResultData.isSuccessful && apiResultData.body() != null){
                    var computeList = arrayListOf<AreaData>()
                    var resultData = apiResultData.body()!!.run {
                        
                        computeList.add(seoul)
                        computeList.add(busan)
                        computeList.add(daegu)
                        computeList.add(incheon)
                        computeList.add(gwangju)
                        computeList.add(daejeon)
                        computeList.add(ulsan)
                        computeList.add(sejong)
                        computeList.add(gyeonggi)
                        computeList.add(gangwon)

                        computeList.add(chungbuk)
                        computeList.add(chungnam)

                        computeList.add(jeonbuk)
                        computeList.add(jeonnam)

                        computeList.add(gyeongbuk)
                        computeList.add(gyeongnam)
                        computeList.add(jeju)
                        computeList.add(quarantine)
                    }
                }else{
                    Log.d(TAG, "getAreaData() Result not Successful or Result.body null")
                }
            }catch (e:Exception){
                Log.d(TAG, "getAreaData() Fail...")
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