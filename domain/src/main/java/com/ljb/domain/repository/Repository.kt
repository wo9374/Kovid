package com.ljb.domain.repository

import com.ljb.domain.NetworkState
import com.ljb.domain.entity.AreaCovid
import com.ljb.domain.entity.SelectiveClinic
import com.ljb.domain.entity.WeekCovid
import kotlinx.coroutines.flow.Flow

interface CovidRepository {
    fun getChartList(): Flow<NetworkState<List<WeekCovid>>>
    fun getAreaList(): Flow<NetworkState<List<AreaCovid>>>
}

interface RemoteClinicRepository{
    fun getRemoteSelectiveClinic(sido:String, sigungu:String): Flow<NetworkState<List<SelectiveClinic>>>
}

interface LocalClinicRepository{
    fun getLocalSelectiveClinic(): List<SelectiveClinic>
    suspend fun insertSelectiveClinic(selectiveClinic: SelectiveClinic)
    suspend fun clearSelectiveClinics()
}