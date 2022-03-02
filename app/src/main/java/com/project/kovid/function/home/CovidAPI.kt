package com.project.kovid.function.home

import com.project.kovid.BuildConfig
import com.project.kovid.model.CovidData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface CovidAPI {
    companion object{
        const val COVID_19_URL = "http://openapi.data.go.kr"
        private const val COVID_19_END_POINT = "openapi/service/rest/Covid19/getCovid19InfStateJson"
    }

    @GET(COVID_19_END_POINT)
    suspend fun getInfo(
        @Query("serviceKey") serviceKey: String = BuildConfig.DATA_GO_KR_API_KEY,
        @Query("startCreateDt") startCreateDt : String,
        @Query("endCreateDt") endCreateDt : String,
    ): Response<CovidData>
}