package com.project.kovid.function.home

import com.project.kovid.BuildConfig
import com.project.kovid.function.news.NaverAPI
import com.project.kovid.model.KoreaCovidCount
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface CovidAPI {
    companion object{
        const val COVID_19_URL = "https://api.corona-19.kr/"
        private const val COVID_19_END_POINT = "korea/country/new/"
        private const val COVID_19_API_KEY = "z2S8EhmVeDkZflIgy4TRWtrF5a3oK6xnL"
    }

    @GET(COVID_19_END_POINT)
    suspend fun getInfo(
        @Query("serviceKey") serviceKey: String = COVID_19_API_KEY
    ): Response<KoreaCovidCount>?
}