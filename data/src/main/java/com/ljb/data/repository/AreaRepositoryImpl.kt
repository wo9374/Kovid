package com.ljb.data.repository

import com.ljb.data.mapper.AreaMapper
import com.ljb.data.repository.remote.datasource.AreaRemoteDataSource
import com.ljb.domain.NetworkState
import com.ljb.domain.entity.Area
import com.ljb.domain.repository.AreaRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * Domain layer의 ChartRepository 상속받아 사용
 */
class AreaRepositoryImpl @Inject constructor(
    private val dataSource: AreaRemoteDataSource
) : AreaRepository{
    override fun getAreaList(): Flow<NetworkState<List<Area>>> {
        return flow {
            val areaResult = dataSource.getCovidArea()

            if (areaResult.isSuccessful){
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
            }else{
                emit(NetworkState.Error(areaResult.message()))
            }
        }
    }
}