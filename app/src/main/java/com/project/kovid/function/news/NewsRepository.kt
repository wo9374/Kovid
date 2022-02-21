package com.project.kovid.function.news

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
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

    suspend fun getNewsData(){
        newsApi.getNewsResult(
            from = "2022-01-21",
            apiKey = BuildConfig.NEW_API_KEY
        )
    }
    suspend fun getData() : ArrayList<News.Article> {
        val reponse = newsApi.getNewsResult(
            from = "2022-01-21",
            apiKey = BuildConfig.NEW_API_KEY
        ).body()

        val list = Gson().fromJson<ArrayList<News.Article>>(reponse!!.articles, object :TypeToken<ArrayList<News.Article>>() {}.type)
        val articlelist = ArrayList<News.Article>()

        for (item in list) {
            articlelist.add(item)
        }
        return articlelist
    }



    private val naverRetrofit : Retrofit = RetrofitService.getRetrofitNaver()
    private val naverApi = naverRetrofit.create(NaverAPI::class.java)

    suspend fun getNaverNewsData() : Response<NaverNews> = naverApi.getNaverSearchResult(
        clientId = BuildConfig.NAVER_CLIENT_ID,
        clientPw = BuildConfig.NAVER_CLIENT_SECRET,
        "news",
        query = URLEncoder.encode("Covid","UTF-8")
    )
}