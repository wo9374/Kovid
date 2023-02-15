package com.ljb.data.di

import com.ljb.data.database.SelectiveClinicDao
import com.ljb.data.remote.api.CovidAPI
import com.ljb.data.remote.api.HospitalAPI
import com.ljb.data.local.datasouce.LocalClinicSource
import com.ljb.data.local.datasouce.LocalClinicSourceImpl
import com.ljb.data.remote.datasource.CovidDataSource
import com.ljb.data.remote.datasource.CovidDataSourceImpl
import com.ljb.data.remote.datasource.RemoteClinicSource
import com.ljb.data.remote.datasource.RemoteClinicSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


/**
 * Hilt로 DataSourcelmpl관련 의존성 주입을 해주기 위한 모듈
 * */
@Module
@InstallIn(SingletonComponent::class)
class DataSourceModule {

    @Provides
    @Singleton
    fun provideCovidDataSource(
        @NetworkModule.ChartType chartApi: CovidAPI,
        @NetworkModule.AreaType areaAPI: CovidAPI
    ): CovidDataSource = CovidDataSourceImpl(chartApi, areaAPI)


    @Provides
    @Singleton
    fun provideRemoteClinicSource(
      @NetworkModule.HospitalType hospitalAPI: HospitalAPI,
      @NetworkModule.NominatimType nominatimAPI: HospitalAPI,
    ): RemoteClinicSource = RemoteClinicSourceImpl(hospitalAPI, nominatimAPI)

    @Provides
    @Singleton
    fun provideLocalClinicSource(dao: SelectiveClinicDao) : LocalClinicSource =
        LocalClinicSourceImpl(dao)
}