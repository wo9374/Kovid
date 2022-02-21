package com.project.kovid

import com.project.kovid.function.home.CovidAPI
import com.project.kovid.function.news.NaverAPI
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitService {

    private lateinit var covidInstance: Retrofit
    private lateinit var naverInstance: Retrofit

    private fun createOkHttpDefault(): OkHttpClient{
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        val client = OkHttpClient.Builder()
            .connectTimeout(20, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .writeTimeout(20, TimeUnit.SECONDS)
            .addInterceptor(httpLoggingInterceptor)
            .build()

        return client
    }

    private fun createOkHttpNaver(): OkHttpClient{
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        val headerInterceptor = Interceptor {
            val request = it.request()
                .newBuilder()
                .addHeader("X-Naver-Client-Id", com.project.kovid.BuildConfig.NAVER_CLIENT_ID)
                .addHeader("X-Naver-Client-Secret", com.project.kovid.BuildConfig.NAVER_CLIENT_SECRET)
                .build()
            return@Interceptor it.proceed(request)
        }

        val client = OkHttpClient.Builder()
            .connectTimeout(20, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .writeTimeout(20, TimeUnit.SECONDS)
            .addInterceptor(headerInterceptor)
            .addInterceptor(httpLoggingInterceptor)
            .build()

        return client
    }

    fun getRetrofitCovid(): Retrofit {
        covidInstance = Retrofit.Builder()
            .baseUrl(CovidAPI.COVID_19_URL)
            .client(createOkHttpDefault())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return covidInstance
    }

    fun getRetrofitNaver(): Retrofit {
        naverInstance = Retrofit.Builder()
            .baseUrl(NaverAPI.NAVER_URL)
            .client(createOkHttpNaver())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return naverInstance
    }
}

