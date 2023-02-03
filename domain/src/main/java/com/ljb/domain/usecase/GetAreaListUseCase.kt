package com.ljb.domain.usecase

import com.ljb.domain.NetworkState
import com.ljb.domain.entity.Area
import com.ljb.domain.repository.AreaRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * repository를 hilt를 이용해 주입받아 하나의 하나의 세부적인 기능을 선언
 * */
class GetAreaListUseCase @Inject constructor(
    private val repository: AreaRepository
) {
    operator fun invoke(): Flow<NetworkState<List<Area>>> = repository.getAreaList()
}