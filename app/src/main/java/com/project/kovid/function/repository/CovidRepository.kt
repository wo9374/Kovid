package com.project.kovid.function.repository

import com.project.kovid.function.home.CovidAPI
import com.project.kovid.model.CovidData
import com.project.kovid.objects.RetrofitObject
import com.project.kovid.util.TimeUtil
import retrofit2.Response

class CovidRepository{
    private val covidRetrofit : retrofit2.Retrofit = RetrofitObject.getRetrofitCovid()

    private val api = covidRetrofit.create(CovidAPI::class.java)

    suspend fun getCovidWeek() : Response<CovidData> = api.getInfo(startCreateDt = TimeUtil.getPast8Day(), endCreateDt = TimeUtil.getTodayDate())
}