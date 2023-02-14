package com.ljb.data.remote

import android.util.Log
import com.ljb.data.mapper.mapperToSelective
import com.ljb.data.remote.datasource.RemoteClinicSource
import com.ljb.domain.NetworkState
import com.ljb.domain.entity.SelectiveClinic
import com.ljb.domain.repository.RemoteClinicRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class RemoteClinicRepositoryImpl @Inject constructor(
    private val remoteSource: RemoteClinicSource,
) : RemoteClinicRepository {
    private val tag = RemoteClinicRepositoryImpl::class.java.simpleName

    override fun getRemoteSelectiveClinic(sido:String): Flow<NetworkState<List<SelectiveClinic>>> {
        return flow {
            val result = remoteSource.getSelectiveClinic(sido, sigungu)
            if (result.isSuccessful){
                Log.d(tag, "${result.body()}")
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