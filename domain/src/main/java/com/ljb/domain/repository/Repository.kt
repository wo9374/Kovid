package com.ljb.domain.repository

import com.ljb.domain.NetworkState
import com.ljb.domain.entity.Area
import com.ljb.domain.entity.WeekCovid
import kotlinx.coroutines.flow.Flow

interface ChartRepository {
    fun getChartList(): Flow<NetworkState<List<WeekCovid>>>
}

interface AreaRepository{
    fun getAreaList(): Flow<NetworkState<List<Area>>>
}