package com.ljb.data.repository.remote.datasource

import com.ljb.data.NetworkModule
//import com.ljb.data.remote.api.AreaAPI
import com.ljb.data.remote.api.CovidAPI
import com.ljb.data.remote.model.AreaData
import com.ljb.data.remote.model.ChartData
import retrofit2.Response
import javax.inject.Inject

/**
 * ChartDataSourceImpl의 실질적 구현체
 * 최종적으로 보면 이곳에서 api 등을 호출하고 다시 view로 보여준다고 보면 됨
 * */
interface CovidDataSource {
    suspend fun getCovidChart(): Response<ChartData>
    suspend fun getCovidArea(): Response<AreaData>
}

class CovidDataSourceImpl @Inject constructor(
    @NetworkModule.ChartType private val chartApi: CovidAPI,
    @NetworkModule.AreaType private val areaAPI: CovidAPI
) : CovidDataSource {
    override suspend fun getCovidChart(): Response<ChartData> = chartApi.getCovidChart()
    override suspend fun getCovidArea(): Response<AreaData> = areaAPI.getCovidArea()
}