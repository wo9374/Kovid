package com.project.kovid.function.home

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.project.kovid.function.repository.CovidRepository
import com.project.kovid.model.CovidItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel(application: Application) : AndroidViewModel(application) {
    private val TAG = HomeViewModel::class.java.simpleName

    private val covidRepo: CovidRepository = CovidRepository()

    val currentDecide = MutableLiveData<List<CovidItem>>()

    fun getCovidItem() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val result = covidRepo.getCovidData()

                if (result.isSuccessful && result.body() != null) {
                    currentDecide.postValue(result.body()!!.body.items.item)
                } else {
                    Log.d(TAG, "getCovidItem() result not Successful or result.body null")
                }
            } catch (e: Exception) {
                Log.d("MainViewModel", "fail...")
            }
        }
    }
}