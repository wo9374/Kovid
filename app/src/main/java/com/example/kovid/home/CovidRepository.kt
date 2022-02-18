package com.example.kovid.home

import com.example.kovid.RetrofitService
import com.example.kovid.model.KoreaCovidCount
import retrofit2.Response
import retrofit2.Retrofit

class CovidRepository{
    private val covidRetrofit : Retrofit = RetrofitService.getRetrofitCovid()

    private val api = covidRetrofit.create(CovidAPI::class.java)

    suspend fun getCovidData() : Response<KoreaCovidCount>? = api.getInfo()
}