package com.ljb.domain.repository

import com.ljb.domain.NetworkState
import com.ljb.domain.entity.Area
import com.ljb.domain.entity.WeekCovid
import kotlinx.coroutines.flow.Flow


/**
 * data 모듈의 ChartRepositoryImpl 가 상속 받는다
 * */
interface ChartRepository {
    suspend fun getChartList(): Flow<NetworkState<List<WeekCovid>>>
}