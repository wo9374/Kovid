package com.example.kovid.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.example.kovid.CovidRepository
import com.example.kovid.model.CovidBody
import com.example.kovid.model.CovidItem

class MainViewModel(application: Application) : AndroidViewModel(application){

    private val covidRepo : CovidRepository = CovidRepository(application)
    private val covidItem = covidRepo.getCovidData()

    fun getCovidData():LiveData<List<CovidItem>>{
        return covidItem
    }
}