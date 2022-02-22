package com.project.kovid.repository

import com.project.kovid.function.home.CovidAPI
import com.project.kovid.objects.RetrofitService
import com.project.kovid.model.KoreaCovidCount
import retrofit2.Response
import retrofit2.Retrofit

class CovidRepository{
    private val covidRetrofit : Retrofit = RetrofitService.getRetrofitCovid()

    private val api = covidRetrofit.create(CovidAPI::class.java)

    suspend fun getCovidData() : Response<KoreaCovidCount>? = api.getInfo()
}