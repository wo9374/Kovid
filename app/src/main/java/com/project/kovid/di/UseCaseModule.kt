package com.project.kovid.di

import com.ljb.domain.repository.CovidRepository
import com.ljb.domain.repository.LocalClinicRepository
import com.ljb.domain.repository.NewRepository
import com.ljb.domain.repository.RemoteClinicRepository
import com.ljb.domain.usecase.ClearSelectiveClinicUseCase
import com.ljb.domain.usecase.GetAreaListUseCase
import com.ljb.domain.usecase.GetChartListUseCase
import com.ljb.domain.usecase.GetDbClinicUseCase
import com.ljb.domain.usecase.GetMapsPolygonUseCase
import com.ljb.domain.usecase.GetNewUseCase
import com.ljb.domain.usecase.GetRemoteClinicUseCase
import com.ljb.domain.usecase.InsertSelectiveClinicUseCase
import com.ljb.domain.usecase.MapJsonParsingUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


/**
 * Hilt로 UseCase관련 의존성 주입을 해주기 위한 모듈
 * */
@Module
@InstallIn(SingletonComponent::class)
class UseCaseModule {

    @Provides
    fun provideGetChartUseCase(repository: CovidRepository) = GetChartListUseCase(repository)
    @Provides
    fun provideGetAreaUseCase(repository: CovidRepository) = GetAreaListUseCase(repository)


    @Provides
    fun provideGetMapsPolygonUseCase(repository: RemoteClinicRepository) = GetMapsPolygonUseCase(repository)
    @Provides
    fun provideGetSelectiveClinicUseCase(repository: RemoteClinicRepository) = GetRemoteClinicUseCase(repository)


    @Provides
    fun provideGetDbSelectiveClinicUseCase(repository: LocalClinicRepository) = GetDbClinicUseCase(repository)
    @Provides
    fun provideInsertSelectiveClinicUseCase(repository: LocalClinicRepository) = InsertSelectiveClinicUseCase(repository)
    @Provides
    fun provideClearSelectiveClinicUseCase(repository: LocalClinicRepository) = ClearSelectiveClinicUseCase(repository)
    @Provides
    fun provideMapJsonParsingUseCase(repository: LocalClinicRepository) = MapJsonParsingUseCase(repository)


    @Provides
    fun provideGetNewsUseCase(repository: NewRepository) = GetNewUseCase(repository)
}