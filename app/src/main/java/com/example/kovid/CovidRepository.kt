package com.example.kovid

import com.example.kovid.model.KoreaCovidCount
import retrofit2.Response
import retrofit2.Retrofit

class CovidRepository{
    private val covidRetrofit : Retrofit = RetrofitService.getRetrofitXmlParsing()

    private val api = covidRetrofit.create(CovidAPI::class.java)

    suspend fun getCovidData() : Response<KoreaCovidCount>? = api.getInfo()
}