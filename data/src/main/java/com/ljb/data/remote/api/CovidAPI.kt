package com.ljb.data.remote.api

import com.ljb.data.BuildConfig
import com.ljb.data.remote.model.AreaData
import com.ljb.data.remote.model.ChartData
import com.ljb.data.util.TimeUtil
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * 일일확진자, 지역별 확진자 Api interface
 * */
interface CovidAPI {
    companion object{
        const val COVID_19_CHART = "http://openapi.data.go.kr"
        private const val COVID_19_END_POINT = "openapi/service/rest/Covid19/getCovid19InfStateJson"

        const val COVID_19_AREA = "https://api.corona-19.kr"
        private const val COVID_19_AREA_END_POINT = "korea/country/new" //?serviceKey=%7BAPI_KEY%7D
    }

    @GET(COVID_19_END_POINT)
    suspend fun getCovidChart(
        @Query("serviceKey") serviceKey: String = BuildConfig.DATA_GO_KR_API_KEY_DECODE,
        @Query("startCreateDt") startCreateDt : String = TimeUtil.getPast1MonthCovid(),
        @Query("endCreateDt") endCreateDt : String = TimeUtil.getTodayDate(),
    ): Response<ChartData>

    @GET(COVID_19_AREA_END_POINT)
    suspend fun getCovidArea(
        @Query("serviceKey") serviceKey: String = BuildConfig.GOODBYE_CORONA_API_KEY
    ): Response<AreaData>
}