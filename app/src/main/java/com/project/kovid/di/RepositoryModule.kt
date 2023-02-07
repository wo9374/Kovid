package com.project.kovid.di

//import com.ljb.data.repository.AreaRepositoryImpl
import com.ljb.data.repository.CovidRepositoryImpl
import com.ljb.data.repository.remote.datasource.CovidDataSourceImpl
import com.ljb.domain.repository.CovidRepository
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
    fun provideCovidRepository(
        covidDataSourceImpl: CovidDataSourceImpl
    ): CovidRepository{
        return CovidRepositoryImpl(
            covidDataSourceImpl
        )
    }

    /*@Provides
    @Singleton
    fun provideAreaRepository(
        areaRemoteDataSourceImpl: AreaDataSourceImpl
    ): AreaRepository{
        return AreaRepositoryImpl(
            areaRemoteDataSourceImpl
        )
    }*/
}