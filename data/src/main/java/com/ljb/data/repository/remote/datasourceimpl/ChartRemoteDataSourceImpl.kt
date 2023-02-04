package com.ljb.data.repository.remote.datasourceimpl

import com.ljb.data.remote.api.CovidAPI
import com.ljb.data.remote.model.ChartData
import com.ljb.data.repository.remote.datasource.ChartDataSource
import retrofit2.Response
import javax.inject.Inject

/**
 * ChartRemoteDataSource 상속받아 사용
 * */
class ChartRemoteDataSourceImpl @Inject constructor(
    private val chartApi: CovidAPI
) : ChartDataSource {
    override suspend fun getCovidChart(): Response<ChartData> {
        return chartApi.getCovidChart()
    }
}