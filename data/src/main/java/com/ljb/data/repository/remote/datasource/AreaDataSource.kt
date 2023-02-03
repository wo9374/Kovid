package com.ljb.data.repository.remote.datasource

import com.ljb.data.remote.model.KoreaArea
import retrofit2.Response

/**
 * ChartDataSourceImpl의 실질적 구현체
 * 최종적으로 보면 이곳에서 api 등을 호출하고 다시 view로 보여준다고 보면 됨
 * */
interface AreaDataSource {
    suspend fun getCovidArea(): Response<KoreaArea>
}