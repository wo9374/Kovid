package com.project.kovid.function.map

import com.project.kovid.model.HospResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface HospitalAPI {
    companion object{
        const val HOSPITAL_URL = "http://apis.data.go.kr/"
        private const val HOSPITAL_END_POINT = "1352000/ODMS_COVID_07/callCovid07Api" //"1352000/ODMS_COVID_07"
    }

    @GET(HOSPITAL_END_POINT)
    suspend fun getHospital(
        @Query("serviceKey") serviceKey : String,
        @Query("pageNo") pageNo : Int,
        @Query("numOfRows") numOfRows : Int,
        @Query("apiType") apiType : String,
        @Query("create_dt") create_dt : String,
        @Query("sido") sido : String,
        @Query("sigungu") sigungu : String,
    ): Response<HospResponse>
}