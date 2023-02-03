package com.project.kovid.di

import com.ljb.data.remote.api.CovidAPI
import com.tickaroo.tikxml.TikXml
import com.tickaroo.tikxml.retrofit.TikXmlConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


/**
 * Hilt로 Retrofit관련 의존성 주입을 해주기 위한 모듈
 * */
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class ChartType

    @Retention(AnnotationRetention.BINARY)

    @Provides
    @Singleton
    fun provideCovidChartRetrofit(
        okHttpClient: OkHttpClient,
        tikXmlConverterFactory: TikXmlConverterFactory
    ): Retrofit{
        //val tikXml = TikXml.Builder().exceptionOnUnreadXml(false).build()

        return Retrofit.Builder()
            .baseUrl(CovidAPI.COVID_19_CHART)
            .client(provideHttpClient())
            .addConverterFactory(tikXmlConverterFactory)
            .build()
    }

    /*@Provides
    @Singleton
    fun provideCovidAreaRetrofit(
        okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory
    ): Retrofit{
        return Retrofit.Builder()
            .baseUrl(CovidAPI.COVID_19_AREA)
            .client(okHttpClient)
            .client(provideHttpClient())
            .addConverterFactory(gsonConverterFactory)
            .build()
    }*/

    @Provides
    @Singleton
        return GsonConverterFactory.create()
        return retrofit.create(CovidAPI::class.java)
    }

    }

    @Provides
    @Singleton
    fun provideTikXmlConverterFactory(): TikXmlConverterFactory{
        return TikXmlConverterFactory.create()
    }

    @Provides
    @Singleton
    fun provideCovidApiService(retrofit: Retrofit): CovidAPI{
        return retrofit.create(CovidAPI::class.java)
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .addInterceptor(getLoggingInterceptor())
            .build()
    }

    private fun getLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
}