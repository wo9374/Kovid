package com.project.kovid.function.news

import androidx.lifecycle.MutableLiveData
import com.project.kovid.model.NaverNews
import com.project.kovid.model.News
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query


interface NewsAPI{
    companion object{
        const val NEWS_BASE_URL = "https://newsapi.org/v2/"

        //지난 달 코로나 확진에 대한 모든 기사, 최근 순으로 정렬
        const val NEWS_FIND_MONTH = "https://newsapi.org/v2/everything?q=코로나 확진&from=2022-01-21&sortBy=publishedAt&apiKey=ff54a0f7cce5421a8da91f60dc9b5056"

        //어제부터 애플을 언급한 모든 기사, 인기 퍼블리셔 순으로 정렬
        const val NEWS_FIND_TOPIC = "https://newsapi.org/v2/everything?q=apple&from=2022-02-20&to=2022-02-20&sortBy=popularity&apiKey=ff54a0f7cce5421a8da91f60dc9b5056"

        //현재 미국의 주요 비즈니스 헤드라인
        const val NEWS_URL5 = "https://newsapi.org/v2/top-headlines?country=kr&category=business&apiKey=ff54a0f7cce5421a8da91f60dc9b5056"

        //한국 헤드라인
        const val NEWS_HEADLINE = "https://newsapi.org/v2/top-headlines?country=kr"
    }

    @GET("everything?q=코로나 확진")
    suspend fun getNewsResult(
        @Query("from") from:String,      //2022-01-21
        @Query("sortBy") sortBy:String = "publishedAt",
        @Query("apiKey") apiKey : String
    ):Response<News>
}


interface NaverAPI {
    companion object{
        const val NAVER_BASE_URL = "https://openapi.naver.com/v1/"
    }

    @GET("search/{type}")
    suspend fun getNaverSearchResult(
        @Header("X-Naver-Client-Id") clientId: String,
        @Header("X-Naver-Client-Secret") clientPw: String,
        @Path("type") type: String,
        @Query("query") query: String
    ): Response<NaverNews>
}
