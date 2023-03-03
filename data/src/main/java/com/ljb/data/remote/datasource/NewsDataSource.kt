package com.ljb.data.remote.datasource

import com.ljb.data.model.NewsResponse
import com.ljb.data.remote.api.NewsAPI
import retrofit2.Response
import javax.inject.Inject

interface NewsDataSource{
    suspend fun getNewsData(query:String):Response<NewsResponse>
}

class NewsDataSourceImpl @Inject constructor(private val newsAPI: NewsAPI) : NewsDataSource{
    override suspend fun getNewsData(query:String): Response<NewsResponse> = newsAPI.getNewsResult()
}