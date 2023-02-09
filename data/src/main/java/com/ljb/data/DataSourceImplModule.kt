package com.ljb.data

import com.ljb.data.remote.api.CovidAPI
import com.ljb.data.remote.api.HospitalAPI
import com.ljb.data.repository.remote.datasource.CovidDataSource
import com.ljb.data.repository.remote.datasource.CovidDataSourceImpl
import com.ljb.data.repository.remote.datasource.HospitalDataSource
import com.ljb.data.repository.remote.datasource.HospitalDataSourceImpl
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
class DataSourceImplModule {

    @Provides
    @Singleton
    fun provideCovidDataSource(
        @NetworkModule.ChartType chartApi: CovidAPI,
        @NetworkModule.AreaType areaAPI: CovidAPI
    ): CovidDataSource {
        return CovidDataSourceImpl(chartApi, areaAPI)
    }

    @Provides
    @Singleton
    fun provideHospitalDataSource(
        hospitalAPI: HospitalAPI
    ): HospitalDataSource{
        return HospitalDataSourceImpl(hospitalAPI)
    }
}