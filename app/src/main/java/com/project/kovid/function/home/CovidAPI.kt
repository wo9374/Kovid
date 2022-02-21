package com.project.kovid.function.home

import com.project.kovid.model.KoreaCovidCount
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface CovidAPI {
    companion object{
        private const val CURRENT_COVID_URL = "http://openapi.data.go.kr/"
        private const val CURRENT_COVID_END_POINT = "openapi/service/rest/Covid19/getCovid19InfStateJson"
        private const val CURRENT_COVID_API_KEY_ENCODE = "fWjOJVhpjM%2B3yRGaxkRfldyTsXfvt2rPzz8D8rqvlkjqU1PUu9heerGe%2F48qtQxsovjakR3TcPbBtufVETkE%2Bg%3D%3D"
        private const val CURRENT_COVID_API_KEY_DECODE = "fWjOJVhpjM+3yRGaxkRfldyTsXfvt2rPzz8D8rqvlkjqU1PUu9heerGe/48qtQxsovjakR3TcPbBtufVETkE+g=="
        /*
         @Query("pageNo") pageNo: Int,
        @Query("numOfRows") numOfRows: Int,
        @Query("startCreateDt") startCreateDt: Int,
        @Query("endCreateDt") endCreateDt: Int,
        @Query("serviceKey") serviceKey: String = CURRENT_COVID_API_KEY_DECODE
        * */


        const val COVID_19_URL = "https://api.corona-19.kr/"
        private const val COVID_19_END_POINT = "korea/country/new/"
        private const val COVID_19_API_KEY = "z2S8EhmVeDkZflIgy4TRWtrF5a3oK6xnL"
    }

    @GET(COVID_19_END_POINT)
    suspend fun getInfo(
        @Query("serviceKey") serviceKey: String = COVID_19_API_KEY
    ): Response<KoreaCovidCount>?
}