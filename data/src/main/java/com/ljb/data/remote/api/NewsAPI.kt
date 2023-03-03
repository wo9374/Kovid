package com.ljb.data.remote.api

import com.ljb.data.BuildConfig
import com.ljb.data.model.NewsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsAPI {/*
    @GET(ApiInfo.SEARCH_END_POINT)
    suspend fun getNaverNews(
        @Header("X-Naver-Client-Id") clientId: String = BuildConfig.NAVER_CLIENT_ID,
        @Header("X-Naver-Client-Secret") clientPw: String = BuildConfig.NAVER_CLIENT_SECRET,
        //@Path("type") type: String ="",
        @Query("query") query: String,
    ): Response<NaverNews>*/

    @GET(ApiInfo.NEWS_END_POINT)
    suspend fun getNewsResult(
        @Query("q") query: String = "코로나",
        //@Query("from") from:String,      //최대 한달전까지 가능
        @Query("sortBy") sortBy:String = "publishedAt",
        @Query("apiKey") apiKey : String = BuildConfig.NEWS_API_KEY
    ):Response<NewsResponse>
}