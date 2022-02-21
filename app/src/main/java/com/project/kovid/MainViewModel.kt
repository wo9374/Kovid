package com.project.kovid

import android.app.Application
import androidx.lifecycle.AndroidViewModel


class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val TAG = MainViewModel::class.java.simpleName

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