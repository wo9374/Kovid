package com.example.kovid

import com.example.kovid.model.CurrentCovid
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface CovidService {
    @GET("getCovid19InfStateJson")
    fun getInfo(
        @Query("serviceKey") serviceKey: String,
        @Query("pageNo") pageNo: Int,
        @Query("numOfRows") numOfRows: Int,
        @Query("startCreateDt") startCreateDt: Int,
        @Query("endCreateDt") endCreateDt: Int
    ): Call<CurrentCovid>
}