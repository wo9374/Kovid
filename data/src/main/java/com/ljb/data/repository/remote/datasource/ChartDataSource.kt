package com.ljb.data.repository.remote.datasource

import com.ljb.data.remote.model.ChartData
import retrofit2.Response

/**
 * ChartRemoteDataSourceImpl의 실질적 구현체
 * 최종적으로 보면 이곳에서 api 등을 호출하고 다시 view로 보여준다고 보면 됨
 * */
interface ChartDataSource {
    suspend fun getCovidChart(): Response<ChartData>
}