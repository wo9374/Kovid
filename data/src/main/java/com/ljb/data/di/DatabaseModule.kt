package com.ljb.data.di

import android.content.Context
import androidx.room.Room
import com.ljb.data.database.ClinicDatabase
import com.ljb.data.database.SelectiveClinicDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    /**
     * Dao instance 생성 명시
     * */
    @Provides
    @Singleton
    fun provideLocalClinicDao(database: ClinicDatabase): SelectiveClinicDao = database.dao()

    /**
     * Database instance 생성 명시
     * */
    @Provides
    @Singleton
    fun provideClinicDatabase(@ApplicationContext appContext: Context): ClinicDatabase =
        Room.databaseBuilder(appContext, ClinicDatabase::class.java, "clinicdb").build()
}