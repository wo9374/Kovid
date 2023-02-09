package com.ljb.data.remote.api

import com.ljb.data.BuildConfig
import com.ljb.data.model.ClinicResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface HospitalAPI {

    @GET(ApiInfo.SELECTIVE_CLINIC_END_POINT)
    suspend fun getSelectiveClinic(
        @Query("serviceKey") serviceKey : String = BuildConfig.DATA_GO_KR_API_KEY_DECODE,
        @Query("pageNo") pageNo : Int = 1,
        @Query("numOfRows") numOfRows : Int = 50,
        @Query("apiType") apiType : String = "xml",
    ): Response<ClinicResponse>
}