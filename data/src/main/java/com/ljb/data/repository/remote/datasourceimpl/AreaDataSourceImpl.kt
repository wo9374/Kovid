package com.ljb.data.repository.remote.datasourceimpl

import com.ljb.data.remote.api.AreaAPI
import com.ljb.data.remote.model.KoreaArea
import com.ljb.data.repository.remote.datasource.AreaDataSource
import retrofit2.Response
import javax.inject.Inject

/**
 * ChartDataSource 상속받아 사용
 * */
class AreaDataSourceImpl @Inject constructor(
    private val areaAPI: AreaAPI
) : AreaDataSource {
    override suspend fun getCovidArea(): Response<KoreaArea>{
        return areaAPI.getCovidArea()
    }
}