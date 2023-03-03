package com.ljb.data.remote.api

import com.ljb.data.BuildConfig
import com.ljb.data.model.PolyGonResponse
import com.ljb.data.model.PolygonOsmId
import com.ljb.data.model.SelectiveClinicJsonResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface HospitalAPI {

    @GET(ApiInfo.SELECTIVE_CLINIC_END_POINT)
    suspend fun getSelectiveClinic(
        @Query("serviceKey") serviceKey: String = BuildConfig.DATA_GO_KR_API_KEY_DECODE,
        @Query("pageNo") pageNo: Int = 1,
        @Query("numOfRows") numOfRows: Int = 100,
        @Query("apiType") apiType: String = ApiInfo.JSON_FORMAT,
        @Query("sido") sido: String,
    ): Response<SelectiveClinicJsonResponse>

    @GET(ApiInfo.TEMPORARY_CLINIC_END_POINT)
    suspend fun getTemporaryClinic(
        @Query("serviceKey") serviceKey: String = BuildConfig.DATA_GO_KR_API_KEY_DECODE,
        @Query("pageNo") pageNo: Int = 1,
        @Query("numOfRows") numOfRows: Int = 100,
        @Query("apiType") apiType: String = ApiInfo.JSON_FORMAT,
        @Query("sido") sido: String,
    ): Response<SelectiveClinicJsonResponse>

    @GET(ApiInfo.OSM_ID_END_POINT)
    suspend fun getPolygonOsmId(
        @Query("q") city: String,
        @Query("format") format: String = ApiInfo.JSON_FORMAT_V2,
    ): List<PolygonOsmId>

    @GET(ApiInfo.POLYGON_END_POINT)
    suspend fun getPolygonGeoJson(
        @Query("osmtype") osmtype: String = "R",
        @Query("osmid") osmid: Long,
        @Query("class") boundary: String = "boundary",
        @Query("addressdetails") addressdetails: Int = 1,
        @Query("hierarchy") hierarchy: Int = 0,
        @Query("group_hierarchy") group_hierarchy: Int = 1,
        @Query("format") format: String = ApiInfo.json_format,
        @Query("polygon_geojson") polygon_geojson: Int = 1,
    ): Response<PolyGonResponse>
}