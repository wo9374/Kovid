package com.ljb.domain.repository

import com.ljb.domain.NetworkState
import com.ljb.domain.entity.AreaCovid
import com.ljb.domain.entity.MapsPolygon
import com.ljb.domain.entity.News
import com.ljb.domain.entity.Clinic
import com.ljb.domain.entity.MapsInfo
import com.ljb.domain.entity.WeekCovid
import kotlinx.coroutines.flow.Flow

interface CovidRepository {
    fun getChartList(): Flow<NetworkState<List<WeekCovid>>>
    fun getAreaList(): Flow<NetworkState<List<AreaCovid>>>
}

interface RemoteClinicRepository{
    fun getMapsPolygon(siDo:String, siGunGu:String): Flow<NetworkState<MapsPolygon>>
    fun getRemoteClinic(clinicType: Int): Flow<NetworkState<List<Clinic>>>
}

interface LocalClinicRepository{
    fun getLocalClinic(siDo: String, siGunGu: String, clinicType: Int): List<Clinic>
    suspend fun insertClinic(clinic: Clinic, clinicType: Int)
    suspend fun clearClinics()

    fun mapInfoJsonParsing(jsonString: String): List<MapsInfo>
}

interface NewRepository{
    fun getNewsData(query: String): Flow<NetworkState<List<News>>>
}