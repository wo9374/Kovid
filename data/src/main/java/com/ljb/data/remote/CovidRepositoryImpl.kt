package com.ljb.data.remote

import com.ljb.data.mapper.AreaMapper
import com.ljb.data.remote.datasource.CovidDataSource
import com.ljb.data.util.computeStringToInt
import com.ljb.domain.NetworkState
import com.ljb.domain.entity.AreaCovid
import com.ljb.domain.entity.WeekCovid
import com.ljb.domain.repository.CovidRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * Domain layer의 ChartRepository 상속받아 사용
 */
class CovidRepositoryImpl @Inject constructor(
    private val dataSource: CovidDataSource
) : CovidRepository {

    override fun getChartList(): Flow<NetworkState<List<WeekCovid>>> {
        return flow {
            val chartResult = dataSource.getCovidChart()

            if (chartResult.isSuccessful) {
                val data = chartResult.body()?.chartBody?.chartItems?.chartItem
                    ?.sortedBy { it.stateDt }
                    //?.map { ChartMapper.mapperToChartItem(it) } //아래 ChartItem -> WeekCovid 로 2차 가공을 위한 매퍼 미사용
                    ?: emptyList()

                //확진날짜, 신규 확진자수 Data Class 생성
                val computeList = arrayListOf<WeekCovid>()
                for (i in 1 until data.size) { // 31일치 구해오기 때문에 -1 계산
                    val currentDate = data[i].stateDt.computeStringToInt()
                    val decideCnt = (data[i].decideCnt - data[i - 1].decideCnt)  //현재날 - 어제날
                    computeList.add(WeekCovid(currentDate, decideCnt))
                }
                emit(NetworkState.Success(computeList))
            } else {
                emit(NetworkState.Error(chartResult.message()))
            }
        }
    }

    override fun getAreaList(): Flow<NetworkState<List<AreaCovid>>> {
        return flow {
            val areaResult = dataSource.getCovidArea()

            if (areaResult.isSuccessful) {
                val data = areaResult.body()!!.run {
                    arrayListOf(
                        seoul, busan, daegu, incheon, gwangju,
                        daejeon, ulsan, sejong, gyeonggi, gangwon,
                        chungbuk, chungnam, jeonbuk, jeonnam, gyeongbuk,
                        gyeongnam, jeju, quarantine
                    ).map {
                        AreaMapper.mapperToArea(it)
                    }.sortedByDescending { it.newCase.replace(",", "").toInt() } //내림차 순
                }
                emit(NetworkState.Success(data))
            } else {
                emit(NetworkState.Error(areaResult.message()))
            }
        }
    }
}