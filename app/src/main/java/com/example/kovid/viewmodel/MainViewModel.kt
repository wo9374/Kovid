package com.example.kovid.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.kovid.CovidRepository
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application){
    private val TAG = MainViewModel::class.java.simpleName
    private val covidRepo : CovidRepository = CovidRepository(application)

    fun getCovidItem(){
        viewModelScope.launch {
            try {
                val result = covidRepo.getCovidData()
            } catch (e: Exception){
                Log.d("MainViewModel", "fail...")
            }
        }
    }
}