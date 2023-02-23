package com.ljb.data.remote

import android.util.Log
import com.ljb.data.mapper.mapperToSelective
import com.ljb.data.remote.datasource.RemoteClinicSource
import com.ljb.domain.NetworkState
import com.ljb.domain.entity.MapsPolygon
import com.ljb.domain.entity.SelectiveClinic
import com.ljb.domain.repository.RemoteClinicRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class RemoteClinicRepositoryImpl @Inject constructor(
    private val remoteSource: RemoteClinicSource,
) : RemoteClinicRepository {
    private val TAG = RemoteClinicRepositoryImpl::class.java.simpleName

    override fun getMapsPolygon(sido: String, sigungu:String): Flow<NetworkState<MapsPolygon>> {
        return flow {
            remoteSource.apply {
                val addr = if (sigungu.isEmpty() || sigungu == "전체")
                    sido
                else "$sido+$sigungu"

                val osmId = getPolygonOsmId(addr) //osmId 먼저 get
                if (osmId.isNotEmpty()){
                    val result = getPolygonData(osmId[0].osm_id) //polygon Data get
                    if (result.isSuccessful){
                        val polygon = result.body()?.geometry?.polygonLatLng?.get(0) ?: emptyList()
                        val centerLatLng = result.body()?.centroid?.centerLatLng ?: emptyList()
                        emit(
                            NetworkState.Success(MapsPolygon(centerLatLng, polygon))
                        )
                    } else
                        emit(NetworkState.Error(result.message()))
                }else
                    emit(NetworkState.Error("OsmId is Empty"))
            }
        }
    }

    override fun getRemoteSelectiveClinic(sido:String): Flow<NetworkState<List<SelectiveClinic>>> {
        return flow {
            val result = remoteSource.getSelectiveClinic(sido)

            if (result.isSuccessful){
                Log.d(TAG, "${result.body()}")
                val data= result.body()?.items?.map {
                    it.mapperToSelective()
                }?: emptyList()

                //XML 파싱
                /*val data = result.body()?.body?.hospitals?.clinicList
                    ?.map { it.mapperToSelective() }
                    ?: emptyList()*/
                emit(NetworkState.Success(data))
            }else
                emit(NetworkState.Error(result.message()))
        }
    }
}