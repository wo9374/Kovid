package com.ljb.data.repository

import com.ljb.data.repository.remote.datasource.ChartRemoteDataSource
import com.ljb.data.util.StringUtil
import com.ljb.domain.NetworkState
import com.ljb.domain.entity.WeekCovid
import com.ljb.domain.repository.ChartRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * Domain layer의 ChartRepository 상속받아 사용
 */
class ChartRepositoryImpl @Inject constructor(
    private val dataSource: ChartRemoteDataSource
) : ChartRepository{

    override fun getChartList(): Flow<NetworkState<List<WeekCovid>>> {
        return flow {
            val chartResult = dataSource.getCovidChart()

            if (chartResult.isSuccessful){
                val data = chartResult.body()?.chartBody?.chartItems?.chartItem
                    ?.sortedBy { it.stateDt }
                    //?.map { ChartMapper.mapperToChartItem(it) } //아래 ChartItem -> WeekCovid 로 2차 가공을 위한 매퍼 미사용
                    ?: emptyList()

                //확진날짜, 신규 확진자수 Data Class 생성
                val computeList = arrayListOf<WeekCovid>()
                for (i in 1 until data.size) { // 31일치 구해오기 때문에 -1 계산
                    val currentDate = StringUtil.computeStringToInt(data[i].stateDt)
                    val decideCnt = (data[i].decideCnt - data[i - 1].decideCnt)  //현재날 - 어제날
                    computeList.add(WeekCovid(currentDate, decideCnt))
                }
                emit(NetworkState.Success(computeList))
            }else{
                emit(NetworkState.Error(chartResult.message()))
            }
        }
    }
}