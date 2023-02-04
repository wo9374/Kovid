package com.ljb.data

import com.ljb.data.remote.api.AreaAPI
import com.ljb.data.remote.api.CovidAPI
import com.ljb.data.repository.remote.datasource.AreaDataSource
import com.ljb.data.repository.remote.datasource.ChartDataSource
import com.ljb.data.repository.remote.datasourceimpl.AreaRemoteDataSourceImpl
import com.ljb.data.repository.remote.datasourceimpl.ChartRemoteDataSourceImpl
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
    fun provideChartDataSource(
        chartApi: CovidAPI
    ) : ChartDataSource {
        return ChartRemoteDataSourceImpl(chartApi)
    }

    @Provides
    @Singleton
    fun provideAreaDataSource(
        areaAPI: AreaAPI
    ) : AreaDataSource {
        return AreaRemoteDataSourceImpl(areaAPI)
    }
}