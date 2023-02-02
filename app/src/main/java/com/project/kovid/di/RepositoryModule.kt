package com.project.kovid.di

import com.ljb.data.repository.ChartRepositoryImpl
import com.ljb.data.repository.remote.datasourceimpl.ChartRemoteDataSourceImpl
import com.ljb.domain.repository.ChartRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


/**
 * Hilt로 Repository관련 의존성 주입을 해주기 위한 모듈
 * */
@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Provides
    @Singleton
    fun provideChartRepository(
        chartRemoteDataSourceImpl: ChartRemoteDataSourceImpl
    ): ChartRepository{
        return ChartRepositoryImpl(
            chartRemoteDataSourceImpl
        )
    }
}