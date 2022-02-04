package com.example.kovid.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.example.kovid.CovidRepository

class MainViewModel(application: Application, repository : CovidRepository) : AndroidViewModel(application){

    /*var openFlag: MutableLiveData<Boolean> = MutableLiveData(false)

    fun floatingOnClick(){
        openFlag.value = openFlag.value?.not()
    }*/
}