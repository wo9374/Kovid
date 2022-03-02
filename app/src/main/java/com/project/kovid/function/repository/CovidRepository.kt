package com.project.kovid.function.repository

import com.project.kovid.function.home.CovidAPI
import com.project.kovid.model.CovidCountData
import com.project.kovid.objects.RetrofitObject
import retrofit2.Response

class CovidRepository{
    private val covidRetrofit : retrofit2.Retrofit = RetrofitObject.getRetrofitCovid()

    private val api = covidRetrofit.create(CovidAPI::class.java)

    suspend fun getCovidData() : Response<CovidCountData> = api.getInfo(startCreateDt = "20220223", endCreateDt = "20220302")
}