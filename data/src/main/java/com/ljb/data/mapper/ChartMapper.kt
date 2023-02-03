package com.ljb.data.mapper

import com.ljb.data.remote.model.AreaResponse
import com.ljb.data.remote.model.ChartResponse
import com.ljb.domain.entity.Area
import com.ljb.domain.entity.ChartItem

/**
 * domain layer는 data layer를 모르기 때문에 data에서 domain layer의 data class로 자료형을 변환
 * */
object ChartMapper {
    fun mapperToChartItem(chartResponse: ChartResponse) : ChartItem {
        return ChartItem(
            createDt = chartResponse.createDt,
            deathCnt = chartResponse.deathCnt,
            decideCnt = chartResponse.deathCnt,
            seq = chartResponse.seq,
            stateDt = chartResponse.stateDt,
            stateTime = chartResponse.stateTime,
            updateDt = chartResponse.updateDt
        )
    }

    fun mapperToChartResponse(chartItem: ChartItem): ChartResponse {
        return ChartResponse(
            createDt = chartItem.createDt,
            deathCnt = chartItem.deathCnt,
            decideCnt = chartItem.deathCnt,
            seq = chartItem.seq,
            stateDt = chartItem.stateDt,
            stateTime = chartItem.stateTime,
            updateDt = chartItem.updateDt
        )
    }
}