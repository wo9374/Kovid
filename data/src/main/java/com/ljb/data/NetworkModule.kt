package com.ljb.data

import com.ljb.data.remote.api.AreaAPI
import com.ljb.data.remote.api.CovidAPI
import com.tickaroo.tikxml.retrofit.TikXmlConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Qualifier
import javax.inject.Singleton


/**
 * Hilt로 Retrofit관련 의존성 주입을 해주기 위한 모듈
 * */
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class ChartType

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class AreaType

    /**
     * CovidApi
     * */
    @Provides
    @Singleton
    @ChartType
    fun provideCovidChartRetrofit(
        tikXmlConverterFactory: TikXmlConverterFactory
    ): Retrofit{
        //val tikXml = TikXml.Builder().exceptionOnUnreadXml(false).build()

        return Retrofit.Builder()
            .baseUrl(CovidAPI.COVID_19_CHART)
            .client(provideHttpClient())
            .addConverterFactory(tikXmlConverterFactory)
            .build()
    }

    /**
     * AreaApi
     * */
    @Provides
    @Singleton
    @AreaType
    fun provideCovidAreaRetrofit(
        gsonConverterFactory: GsonConverterFactory
    ): Retrofit{
        return Retrofit.Builder()
            .baseUrl(AreaAPI.COVID_19_AREA)
            .client(provideHttpClient())
            .addConverterFactory(gsonConverterFactory)
            .build()
    }

    /**
     * CovidService
     * */
    @Provides
    @Singleton
    fun provideCovidChartService(@ChartType retrofit: Retrofit): CovidAPI{
        return retrofit.create(CovidAPI::class.java)
    }

    /**
     * AreaService
     * */
    @Provides
    @Singleton
    fun provideCovidAreaService(@AreaType retrofit: Retrofit): AreaAPI{
        return retrofit.create(AreaAPI::class.java)
    }

    @Provides
    @Singleton
    fun provideTikXmlConverterFactory(): TikXmlConverterFactory{
        return TikXmlConverterFactory.create()
    }


    @Provides
    @Singleton
    fun provideConverterFactory(): GsonConverterFactory {
        return GsonConverterFactory.create()
    }

    fun provideHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .addInterceptor(getLoggingInterceptor())
            .build()
    }

    private fun getLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
}