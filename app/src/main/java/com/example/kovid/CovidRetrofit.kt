package com.example.kovid

import com.example.kovid.model.CurrentCovid
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.http.GET

object CovidRetrofit {
    private const val CURRENT_COVID_URL = "http://openapi.data.go.kr/"

    private const val CURRENT_COVID_API_END_POINT = "openapi/service/rest/Covid19/getCovid19InfStateJson"

    private const val CURRENT_COVID_API_KEY = "fWjOJVhpjM%2B3yRGaxkRfldyTsXfvt2rPzz8D8rqvlkjqU1PUu9heerGe%2F48qtQxsovjakR3TcPbBtufVETkE%2Bg%3D%3D"

    //http://openapi.data.go.kr/openapi/service/rest/Covid19/getCovid19InfStateJson?serviceKey=인증키(URL Encode)&pageNo=1&numOfRows=10&startCreateDt=20200310&endCreateDt=20200315

    val interceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }
    val client = OkHttpClient.Builder()
        .addInterceptor(interceptor)
        .build()

   // private val retrofit : Retrofit = Retrofit.Builder().baseUrl(CURRENT_COVID_URL)

    //Todo Api 통신 구현중...
    interface CovidApiService{
        @GET(CURRENT_COVID_API_END_POINT)
        fun getInfo():Call<CurrentCovid>
    }
}

