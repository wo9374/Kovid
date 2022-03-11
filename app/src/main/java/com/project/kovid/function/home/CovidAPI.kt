package com.project.kovid.function.home

import com.project.kovid.BuildConfig
import com.project.kovid.model.CovidAreaData
import com.project.kovid.model.CovidChartData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface CovidAPI {
    companion object{
        const val COVID_19_CHART = "http://openapi.data.go.kr"
        private const val COVID_19_END_POINT = "openapi/service/rest/Covid19/getCovid19InfStateJson"

        const val COVID_19_AREA = "https://api.corona-19.kr"
        private const val COVID_19_AREA_END_POINT = "korea/country/new" //?serviceKey=%7BAPI_KEY%7D
    }

    @GET(COVID_19_END_POINT) suspend fun getCovidChartInfo(
        @Query("serviceKey") serviceKey: String = BuildConfig.DATA_GO_KR_API_KEY,
        @Query("startCreateDt") startCreateDt : String,
        @Query("endCreateDt") endCreateDt : String,
    ): Response<CovidChartData>


    @GET(COVID_19_AREA_END_POINT) suspend fun getCovidArea(
        @Query("serviceKey") serviceKey: String = BuildConfig.GOODBYE_CORONA_API_KEY
    ):Response<CovidAreaData>
}