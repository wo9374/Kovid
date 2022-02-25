package com.project.kovid.function.map

import com.project.kovid.model.HospData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface HospitalAPI {
    companion object{
        const val HOSPITAL_URL = "http://apis.data.go.kr/"
        private const val HOSPITAL_END_POINT = "B551182/pubReliefHospService/getpubReliefHospList"

        //B551182/pubReliefHospService/getpubReliefHospList
    }

    @GET(HOSPITAL_END_POINT)
    suspend fun getHospital(
        @Query("serviceKey") serviceKey : String,
        @Query("pageNo") pageNo : Int,
        @Query("numOfRows") numOfRows : Int,
        @Query("spclAdmTyCd") spclAdmTyCd : String,
    ): Response<HospData>
}