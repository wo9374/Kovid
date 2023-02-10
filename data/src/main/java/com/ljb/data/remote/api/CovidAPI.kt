package com.ljb.data.remote.api

import com.ljb.data.BuildConfig
import com.ljb.data.model.AreaData
import com.ljb.data.model.ChartData
import com.ljb.data.util.getPast1MonthCovid
import com.ljb.data.util.getTodayDateString
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.*

/**
 * 일일확진자, 지역별 확진자 Api interface
 * */
interface CovidAPI {
    @GET(ApiInfo.COVID_CHART_END_POINT)
    suspend fun getCovidChart(
        @Query("serviceKey") serviceKey: String = BuildConfig.DATA_GO_KR_API_KEY_DECODE,
        @Query("startCreateDt") startCreateDt : String = Date().getPast1MonthCovid(),
        @Query("endCreateDt") endCreateDt : String = Date().getTodayDateString(),
    ): Response<ChartData>

    @GET(ApiInfo.COVID_AREA_END_POINT)
    suspend fun getCovidArea(
        @Query("serviceKey") serviceKey: String = BuildConfig.GOODBYE_CORONA_API_KEY
    ): Response<AreaData>
}