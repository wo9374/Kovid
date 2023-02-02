package com.ljb.domain.usecase

import com.ljb.domain.NetworkState
import com.ljb.domain.entity.WeekCovid
import com.ljb.domain.repository.ChartRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * repository를 hilt를 이용해 주입받아 하나의 하나의 세부적인 기능을 선언
 * */
class GetChartListUseCase @Inject constructor(
    private val repository: ChartRepository
) {
    suspend fun invoke(): Flow<NetworkState<List<WeekCovid>>> = repository.getChartList()
}