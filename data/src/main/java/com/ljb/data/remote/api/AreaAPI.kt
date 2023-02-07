package com.ljb.data.remote.api

import com.ljb.data.BuildConfig
import com.ljb.data.remote.model.AreaData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface AreaAPI {
    companion object {
        const val COVID_19_AREA = "https://api.corona-19.kr"
        private const val COVID_19_AREA_END_POINT = "korea/country/new" //?serviceKey=%7BAPI_KEY%7D
    }

    @GET(COVID_19_AREA_END_POINT)
    suspend fun getCovidArea(
        @Query("serviceKey") serviceKey: String = BuildConfig.GOODBYE_CORONA_API_KEY
    ): Response<AreaData>
}