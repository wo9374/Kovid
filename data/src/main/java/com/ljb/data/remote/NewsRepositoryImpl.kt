package com.ljb.data.remote

import com.ljb.data.mapper.mapperToNews
import com.ljb.data.remote.datasource.NewsDataSource
import com.ljb.domain.NetworkState
import com.ljb.domain.entity.News
import com.ljb.domain.repository.NewRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class NewsRepositoryImpl @Inject constructor(
    private val newsDataSource: NewsDataSource
): NewRepository {
    override fun getNewsData(query:String): Flow<NetworkState<List<News>>> {
        return flow {
            val result = newsDataSource.getNewsData(query)

            if (result.isSuccessful){
                val data = result.body()?.newsInfos?.map {
                    it.mapperToNews()
                } ?: emptyList()

                emit(NetworkState.Success(data))
            }else{
                emit(NetworkState.Error(result.message()))
            }
        }
    }
}