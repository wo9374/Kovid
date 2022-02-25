package com.project.kovid.function.repository

import com.project.kovid.BuildConfig
import com.project.kovid.function.news.NaverAPI
import com.project.kovid.function.news.NewsAPI
import com.project.kovid.model.NaverNews
import com.project.kovid.model.NewsData
import com.project.kovid.objects.RetrofitObject
import retrofit2.Response
import java.net.URLEncoder

class NewsRepository {
    private val newsRetrofit : retrofit2.Retrofit = RetrofitObject.getRetrofitNews()
    private val newsApi = newsRetrofit.create(NewsAPI::class.java)

    suspend fun getNewsData(data : String) : Response<NewsData> =newsApi.getNewsResult(from = data, apiKey = BuildConfig.NEW_API_KEY)


    private val naverRetrofit : retrofit2.Retrofit = RetrofitObject.getRetrofitNaver()
    private val naverApi = naverRetrofit.create(NaverAPI::class.java)

    suspend fun getNaverNewsData() : Response<NaverNews> = naverApi.getNaverSearchResult(
        clientId = BuildConfig.NAVER_CLIENT_ID,
        clientPw = BuildConfig.NAVER_CLIENT_SECRET,
        "news",
        query = URLEncoder.encode("Covid","UTF-8")
    )
}