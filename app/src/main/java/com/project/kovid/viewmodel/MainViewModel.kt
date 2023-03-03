package com.project.kovid.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.flow.MutableStateFlow


class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val TAG = MainViewModel::class.java.simpleName

    val botNaviViewVisibility = MutableStateFlow(true)
}