package com.ljb.data

import com.ljb.data.remote.api.ApiInfo
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

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class HospType


    @ChartType
    @Provides
    @Singleton
    fun provideCovidChartRetrofit(
        okHttpClient: OkHttpClient,
        tikXmlConverterFactory: TikXmlConverterFactory,
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(ApiInfo.COVID_CHART_URL)
            .client(okHttpClient)
            .addConverterFactory(tikXmlConverterFactory)
            .build()
    }

    @AreaType
    @Provides
    @Singleton
    fun provideCovidAreaRetrofit(
        okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory,
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(ApiInfo.COVID_AREA_URL)
            .client(okHttpClient)
            .addConverterFactory(gsonConverterFactory)
            .build()
    }

    @ChartType
    @Provides
    @Singleton
    fun provideCovidChartService(@ChartType retrofit: Retrofit): CovidAPI {
        return retrofit.create(CovidAPI::class.java)
    }

    @AreaType
    @Provides
    @Singleton
    fun provideCovidAreaService(@AreaType retrofit: Retrofit): CovidAPI {
        return retrofit.create(CovidAPI::class.java)
    }


    //공용 Gson
    @Provides
    @Singleton
    fun provideConverterFactory(): GsonConverterFactory {
        return GsonConverterFactory.create()
    }

    //공용 TikXml
    @Provides
    @Singleton
    fun provideTikXmlConverterFactory(): TikXmlConverterFactory {
        return TikXmlConverterFactory.create()
    }

    //공용 okHttp
    @Provides
    @Singleton
    fun provideHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()
    }
}