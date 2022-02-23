package com.project.kovid

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData


class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val TAG = MainViewModel::class.java.simpleName

    val visibility = MutableLiveData(true)

    fun onBottomNavi(){
        visibility.postValue(true)
    }

    fun offBottomNavi(){
        visibility.postValue(false)
    }

    //private val covidRepo: CovidRepository = CovidRepository()


    /*fun getCovidItem() {
        viewModelScope.launch {
            try {
                val result = covidRepo.getCovidData()
            } catch (e: Exception) {
                Log.d("MainViewModel", "fail...")
            }
        }
    }*/
}