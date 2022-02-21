package com.project.kovid.function.news

import com.project.kovid.model.KovidNews
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface NaverAPI {
    companion object{
        const val NAVER_URL = "https://openapi.naver.com/v1/"
    }

    @GET("search/{type}")
    suspend fun getSearchResult(
        @Header("X-Naver-Client-Id") clientId: String,
        @Header("X-Naver-Client-Secret") clientPw: String,
        @Path("type") type: String,
        @Query("query") query: String
    ): Response<KovidNews>
}