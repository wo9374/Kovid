package com.ljb.data.repository

import android.util.Log
import com.ljb.data.mapper.mapperToSelective
import com.ljb.data.repository.remote.datasource.HospitalDataSource
import com.ljb.domain.NetworkState
import com.ljb.domain.entity.SelectiveClinic
import com.ljb.domain.repository.HospitalRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class HospitalRepositoryImpl @Inject constructor(
    private val dataSource: HospitalDataSource,
) : HospitalRepository {
    private val tag = HospitalRepositoryImpl::class.java.simpleName

    override fun getSelectiveClinic(): Flow<NetworkState<List<SelectiveClinic>>> {
        return flow {
            val result = dataSource.getSelectiveClinic()
            if (result.isSuccessful){
                Log.d(tag, "${result.body()}")

                val data = result.body()?.body?.hospitals?.clinicList
                    ?.map { it.mapperToSelective() }
                    ?: emptyList()
                emit(NetworkState.Success(data))
            }else
                emit(NetworkState.Error(result.message()))
        }
    }
}