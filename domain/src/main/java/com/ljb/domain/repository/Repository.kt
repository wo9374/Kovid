package com.ljb.domain.repository

import com.ljb.domain.NetworkState
import com.ljb.domain.entity.AreaCovid
import com.ljb.domain.entity.MapsPolygon
import com.ljb.domain.entity.News
import com.ljb.domain.entity.SelectiveClinic
import com.ljb.domain.entity.WeekCovid
import kotlinx.coroutines.flow.Flow

interface CovidRepository {
    fun getChartList(): Flow<NetworkState<List<WeekCovid>>>
    fun getAreaList(): Flow<NetworkState<List<AreaCovid>>>
}

interface RemoteClinicRepository{
    fun getMapsPolygon(siDo:String, siGunGu:String): Flow<NetworkState<MapsPolygon>>
    fun getRemoteSelectiveClinic(): Flow<NetworkState<List<SelectiveClinic>>>
    fun getRemoteTemporaryClinic(): Flow<NetworkState<List<SelectiveClinic>>>
}

interface LocalClinicRepository{
    fun getLocalClinic(siDo: String, siGunGu: String, clinicType: Int): List<SelectiveClinic>
    suspend fun insertClinic(selectiveClinic: SelectiveClinic, clinicType: Int)
    suspend fun clearClinics()
}

interface NewRepository{
    fun getNewsData(query: String): Flow<NetworkState<List<News>>>
}