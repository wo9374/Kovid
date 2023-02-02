package com.project.kovid.di

import com.ljb.domain.repository.ChartRepository
import com.ljb.domain.usecase.GetChartListUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


/**
 * Hilt로 UseCase관련 의존성 주입을 해주기 위한 모듈
 * */
@Module
@InstallIn(SingletonComponent::class)
class UseCaseModule {

    @Provides
    @Singleton
    fun provideGetChartListUseCase(repository: ChartRepository) = GetChartListUseCase(repository)
}