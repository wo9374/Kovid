package com.example.kovid

import android.app.Application
import com.example.kovid.model.KoreaCovidCount
import retrofit2.Response
import retrofit2.Retrofit

class CovidRepository(application: Application){

    private val covidRetrofit : Retrofit = RetrofitService.getRetrofitXmlParsing()

    private val api = covidRetrofit.create(CovidAPI::class.java)

    suspend fun getCovidData() : Response<KoreaCovidCount>? = api.getInfo()
}