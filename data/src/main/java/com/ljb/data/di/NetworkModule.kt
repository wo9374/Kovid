package com.ljb.data.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.ljb.data.remote.api.ApiInfo
import com.ljb.data.remote.api.CovidAPI
import com.ljb.data.remote.api.HospitalAPI
import com.ljb.data.remote.api.NewsAPI
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
    annotation class HospitalType

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class PolygonType


    @ChartType
    @Provides
    @Singleton
    fun provideCovidChartRetrofit(
        okHttpClient: OkHttpClient,
        tikXmlConverterFactory: TikXmlConverterFactory,
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(ApiInfo.CHART_URL)
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
            .baseUrl(ApiInfo.AREA_URL)
            .client(okHttpClient)
            .addConverterFactory(gsonConverterFactory)
            .build()
    }

    @HospitalType
    @Provides
    @Singleton
    fun provideHospitalRetrofit(
        okHttpClient: OkHttpClient,
        //tikXmlConverterFactory: TikXmlConverterFactory,
        gsonConverterFactory: GsonConverterFactory,
    ): Retrofit{
        return Retrofit.Builder()
            .baseUrl(ApiInfo.HOSPITAL_URL)
            .client(okHttpClient)
            .addConverterFactory(gsonConverterFactory)
            .build()
    }

    @PolygonType
    @Provides
    @Singleton
    fun provideNominatimRetrofit(
        okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory,
    ): Retrofit{
        return Retrofit.Builder()
            .baseUrl(ApiInfo.NOMINATIM_URL)
            .client(okHttpClient)
            .addConverterFactory(gsonConverterFactory)
            .build()
    }

    @Provides
    @Singleton
    fun provideNewsRetrofit(
        okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory,
    ): Retrofit{
        return Retrofit.Builder()
            .baseUrl(ApiInfo.NEWS_URL)
            .client(okHttpClient)
            .addConverterFactory(gsonConverterFactory)
            .build()
    }

    @ChartType
    @Provides
    @Singleton
    fun provideCovidChartService(@ChartType retrofit: Retrofit): CovidAPI =
        retrofit.create(CovidAPI::class.java)

    @AreaType
    @Provides
    @Singleton
    fun provideCovidAreaService(@AreaType retrofit: Retrofit): CovidAPI =
        retrofit.create(CovidAPI::class.java)

    @HospitalType
    @Provides
    @Singleton
    fun provideHospitalService(@HospitalType retrofit: Retrofit): HospitalAPI =
        retrofit.create(HospitalAPI::class.java)

    @PolygonType
    @Provides
    @Singleton
    fun provideNominatimService(@PolygonType retrofit: Retrofit): HospitalAPI =
        retrofit.create(HospitalAPI::class.java)

    @Provides
    @Singleton
    fun provideNewsService(retrofit: Retrofit): NewsAPI = retrofit.create(NewsAPI::class.java)



    //공용 Gson
    @Provides
    fun provideConverterFactory(): GsonConverterFactory {
        val gson : Gson = GsonBuilder()
            .setLenient()
            .create()
        return GsonConverterFactory.create(gson)
    }

    //공용 TikXml
    @Provides
    fun provideTikXmlConverterFactory(): TikXmlConverterFactory =TikXmlConverterFactory.create()

    //공용 okHttp
    @Provides
    fun provideHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()
    }
}