package com.ljb.data.remote.api

import com.ljb.data.BuildConfig
import com.ljb.data.remote.model.ChartData
import com.ljb.data.util.TimeUtil
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query


/**
 * retrofit을 사용할 때 필요한 인터페이스
 * @GET 어노테이션을 사용하고 반환형은 Response<ChartData>
 * */
interface CovidAPI {
    companion object{
        const val COVID_19_CHART = "http://openapi.data.go.kr"
        private const val COVID_19_END_POINT = "openapi/service/rest/Covid19/getCovid19InfStateJson"
    }

    @GET(COVID_19_END_POINT)
    suspend fun getCovidChart(
        @Query("serviceKey") serviceKey: String = BuildConfig.DATA_GO_KR_API_KEY,
        @Query("startCreateDt") startCreateDt : String = TimeUtil.getPast1MonthCovid(),
        @Query("endCreateDt") endCreateDt : String = TimeUtil.getTodayDate(),
    ): Response<ChartData>
}