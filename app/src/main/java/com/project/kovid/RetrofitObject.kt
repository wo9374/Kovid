package com.project.kovid

import com.project.kovid.function.news.NaverAPI
import com.project.kovid.function.news.NewsAPI
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitObject {

    private lateinit var covidInstance: Retrofit
    private lateinit var naverInstance: Retrofit
    private lateinit var newsInstance: Retrofit
    private lateinit var hospitalInstance: Retrofit

    private fun createOkHttpDefault(): OkHttpClient{
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        val client = OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .addInterceptor(httpLoggingInterceptor)
            .build()

        return client
    }

    fun getRetrofitNews(): Retrofit {
        newsInstance = Retrofit.Builder()
            .baseUrl(NewsAPI.NEWS_BASE_URL)
            .client(createOkHttpDefault())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return newsInstance
    }

    /*fun getRetrofitCovidChart(): Retrofit {
        val tikXml = TikXml.Builder().exceptionOnUnreadXml(false).build()
        covidInstance = Retrofit.Builder()
            .baseUrl(CovidAPI.COVID_19_CHART)
            .client(createOkHttpDefault())
            //.addConverterFactory(GsonConverterFactory.create())
            .addConverterFactory(TikXmlConverterFactory.create(tikXml))
            .build()
        return covidInstance
    }

    fun getRetrofitCovidArea(): Retrofit {
        covidInstance = Retrofit.Builder()
            .baseUrl(CovidAPI.COVID_19_AREA)
            .client(createOkHttpDefault())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return covidInstance
    }*/

    /*fun getRetrofitHospital(): Retrofit {
        val tikXml = TikXml.Builder().exceptionOnUnreadXml(false).build()
        hospitalInstance = Retrofit.Builder()
            .baseUrl(HospitalAPI.HOSPITAL_URL)
            .client(createOkHttpDefault())
            .addConverterFactory(TikXmlConverterFactory.create(tikXml))
            .build()
        return hospitalInstance
    }*/

    private fun createOkHttpNaver(): OkHttpClient{
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        val headerInterceptor = Interceptor {
            val request = it.request()
                .newBuilder()
                .addHeader("X-Naver-Client-Id", BuildConfig.NAVER_CLIENT_ID)
                .addHeader("X-Naver-Client-Secret", BuildConfig.NAVER_CLIENT_SECRET)
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

    fun getRetrofitNaver(): Retrofit {
        naverInstance = Retrofit.Builder()
            .baseUrl(NaverAPI.NAVER_BASE_URL)
            .client(createOkHttpNaver())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return naverInstance
    }
}
