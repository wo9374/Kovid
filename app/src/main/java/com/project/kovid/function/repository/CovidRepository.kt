package com.project.kovid.function.repository

import com.project.kovid.function.home.CovidAPI
import com.project.kovid.model.CovidData
import com.project.kovid.objects.RetrofitObject
import retrofit2.Response

class CovidRepository{
    private val covidRetrofit : retrofit2.Retrofit = RetrofitObject.getRetrofitCovid()

    private val api = covidRetrofit.create(CovidAPI::class.java)

    suspend fun getCovidData() : Response<CovidData> = api.getInfo(startCreateDt = "20220228", endCreateDt = "20220301")
}