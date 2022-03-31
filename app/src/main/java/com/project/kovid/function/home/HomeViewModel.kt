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

    private var resultDecide = ArrayList<WeekCovid>()
    val currentDecide = MutableLiveData<ArrayList<WeekCovid>>()

    var areaDecide = MutableLiveData<ArrayList<ArrayList<AreaData>>>()

    fun getChartData() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
            val apiResultData = covidRepo.getCovidChartData()
            if (apiResultData.isSuccessful && apiResultData.body() != null) {

                val resultData = apiResultData.body()!!.chartBody.chartItems.chartItem.sortedBy { it.stateDt }  //오름차순

                val computeList = arrayListOf<WeekCovid>()
                for (i in 1 until resultData.size) { // 31일치 구해오기 때문에 -1 계산
                    val currentDate = StringUtil.computeStringToInt(resultData[i].stateDt)
                    val decideCnt = (resultData[i].decideCnt - resultData[i - 1].decideCnt)  //현재날 - 어제날
                    computeList.add(WeekCovid(currentDate, decideCnt))
                }
                resultDecide = computeList

                weekDataSet() //초기 일주일 set

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

    fun getAreaData() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val apiResultData = covidRepo.getCovidAreaData()
                if (apiResultData.isSuccessful && apiResultData.body() != null) {

                    apiResultData.body()!!.run {
                        val sortList = arrayListOf(
                            seoul, busan, daegu, incheon, gwangju,
                            daejeon, ulsan, sejong, gyeonggi, gangwon,
                            chungbuk, chungnam, jeonbuk, jeonnam, gyeongbuk,
                            gyeongnam, jeju, quarantine
                        )
                        //내림차 순
                        sortList.sortByDescending { it.newCase.replace(",", "").toInt() }

                        val viewPagerList = arrayListOf<ArrayList<AreaData>>()

                        val recyclerList1 = arrayListOf<AreaData>()
                        val recyclerList2 = arrayListOf<AreaData>()
                        val recyclerList3 = arrayListOf<AreaData>()
                        for (i in 0 until sortList.size) {
                            if (i < 6) {
                                recyclerList1.add(sortList[i])
                            } else if (i < 12) {
                                recyclerList2.add(sortList[i])
                            } else {
                                recyclerList3.add(sortList[i])
                            }
                        }

                        viewPagerList.add(recyclerList1)
                        viewPagerList.add(recyclerList2)
                        viewPagerList.add(recyclerList3)

                        areaDecide.postValue(viewPagerList)
                    }
                } else {
                    Log.d(TAG, "getAreaData() Result not Successful or Result.body null")
                }
            } catch (e: Exception) {
                Log.d(TAG, "getAreaData() Fail...")
            }
        }
    }

    fun weekDataSet() {
        if (resultDecide.size != 0) {
            val weekCovid = arrayListOf<WeekCovid>()
            for (i in resultDecide.size - 7 until resultDecide.size) {
                weekCovid.add(resultDecide[i])
            }
            currentDecide.postValue(weekCovid)
        }
    }

    fun monthDataSet() {
        if (resultDecide.size != 0)
            currentDecide.postValue(resultDecide)
    }
}