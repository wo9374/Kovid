package com.ljb.data.di

import com.ljb.data.remote.CovidRepositoryImpl
import com.ljb.data.local.LocalClinicRepositoryImpl
import com.ljb.data.local.datasouce.LocalClinicSource
import com.ljb.data.remote.NewsRepositoryImpl
import com.ljb.data.remote.RemoteClinicRepositoryImpl
import com.ljb.data.remote.datasource.CovidDataSourceImpl
import com.ljb.data.remote.datasource.NewsDataSourceImpl
import com.ljb.data.remote.datasource.RemoteClinicSourceImpl
import com.ljb.domain.repository.CovidRepository
import com.ljb.domain.repository.LocalClinicRepository
import com.ljb.domain.repository.NewRepository
import com.ljb.domain.repository.RemoteClinicRepository
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
    fun provideCovidRepository(covidDataSourceImpl: CovidDataSourceImpl): CovidRepository =
        CovidRepositoryImpl(covidDataSourceImpl)

    @Provides
    @Singleton
    fun provideClinicRepository(remoteClinicSourceImpl: RemoteClinicSourceImpl): RemoteClinicRepository =
        RemoteClinicRepositoryImpl(remoteClinicSourceImpl)

    @Provides
    @Singleton
    fun provideLocalClinicRepository(localClinicSource: LocalClinicSource) : LocalClinicRepository =
        LocalClinicRepositoryImpl(localClinicSource)

    @Provides
    @Singleton
    fun provideNewsRepository(newsDataSourceImpl: NewsDataSourceImpl) : NewRepository =
        NewsRepositoryImpl(newsDataSourceImpl)
}