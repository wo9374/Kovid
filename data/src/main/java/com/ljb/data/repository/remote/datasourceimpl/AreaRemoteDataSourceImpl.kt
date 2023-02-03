package com.ljb.data.repository.remote.datasourceimpl

import com.ljb.data.remote.api.AreaAPI
import com.ljb.data.remote.model.KoreaArea
import com.ljb.data.repository.remote.datasource.AreaRemoteDataSource
import retrofit2.Response
import javax.inject.Inject

/**
 * ChartRemoteDataSource 상속받아 사용
 * */
class AreaRemoteDataSourceImpl @Inject constructor(
    private val areaAPI: AreaAPI
) : AreaRemoteDataSource {
    override suspend fun getCovidArea(): Response<KoreaArea>{
        return areaAPI.getCovidArea()
    }
}