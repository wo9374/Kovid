package com.project.kovid.function.news

import com.project.kovid.BuildConfig
import com.project.kovid.RetrofitService
import com.project.kovid.model.KovidNews
import retrofit2.Response
import retrofit2.Retrofit
import java.net.URLEncoder

class NewsRepository {
    private val newsRetrofit : Retrofit = RetrofitService.getRetrofitNaver()

    private val api = newsRetrofit.create(NaverAPI::class.java)

    suspend fun getNaverNewsData() : Response<KovidNews> = api.getSearchResult(
        clientId = BuildConfig.NAVER_CLIENT_ID,
        clientPw = BuildConfig.NAVER_CLIENT_SECRET,
        "news",
        query = URLEncoder.encode("Covid","UTF-8")
    )
}