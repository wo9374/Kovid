package com.project.kovid.function.repository

import com.project.kovid.function.home.CovidAPI
import com.project.kovid.model.KoreaArea
import com.project.kovid.model.CovidChartData
import com.project.kovid.objects.RetrofitObject
import com.project.kovid.util.TimeUtil
import retrofit2.Response

class CovidRepository{
    private val chartRetrofit : retrofit2.Retrofit = RetrofitObject.getRetrofitCovidChart()
    private val areaRetrofit : retrofit2.Retrofit = RetrofitObject.getRetrofitCovidArea()

    private val covidChartApi = chartRetrofit.create(CovidAPI::class.java)
    private val covidAreaApi = areaRetrofit.create(CovidAPI::class.java)

    suspend fun getCovidChartData() : Response<CovidChartData> = covidChartApi.getCovidChartInfo(startCreateDt = TimeUtil.getPast1MonthCovid(), endCreateDt = TimeUtil.getTodayDate())

    suspend fun getCovidAreaData() : Response<KoreaArea> = covidAreaApi.getCovidArea()
}