package com.project.kovid.function.news

import com.project.kovid.BuildConfig
import com.project.kovid.RetrofitService
import com.project.kovid.model.NaverNews
import com.project.kovid.model.News
import retrofit2.Response
import retrofit2.Retrofit
import java.net.URLEncoder

class NewsRepository {
    private val newsRetrofit : Retrofit = RetrofitService.getRetrofitNews()
    private val newsApi = newsRetrofit.create(NewsAPI::class.java)

    suspend fun getNewsData(data : String) : Response<News> =newsApi.getNewsResult(from = data, apiKey = BuildConfig.NEW_API_KEY)


    private val naverRetrofit : Retrofit = RetrofitService.getRetrofitNaver()
    private val naverApi = naverRetrofit.create(NaverAPI::class.java)

    suspend fun getNaverNewsData() : Response<NaverNews> = naverApi.getNaverSearchResult(
        clientId = BuildConfig.NAVER_CLIENT_ID,
        clientPw = BuildConfig.NAVER_CLIENT_SECRET,
        "news",
        query = URLEncoder.encode("Covid","UTF-8")
    )
}