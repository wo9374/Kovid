package com.ljb.data.remote.datasource

import com.ljb.data.di.NetworkModule
import com.ljb.data.model.PolygonOsmId
import com.ljb.data.model.PolyGonResponse
import com.ljb.data.remote.api.HospitalAPI
import com.ljb.data.model.SelectiveClinicJsonResponse
import retrofit2.Response
import javax.inject.Inject


interface RemoteClinicSource {
    suspend fun getSelectiveClinic(sido:String, sigungu:String=""): Response<SelectiveClinicJsonResponse>
    suspend fun getPolygonOsmId(adminArea: String): List<PolygonOsmId>
    suspend fun getPolygonData(osm_id : Long): Response<PolyGonResponse>
}

class RemoteClinicSourceImpl @Inject constructor(
    @NetworkModule.HospitalType private val hospitalAPI: HospitalAPI,
    @NetworkModule.NominatimType private val polygonAPI: HospitalAPI,
) : RemoteClinicSource {
    override suspend fun getSelectiveClinic(sido:String, sigungu:String): Response<SelectiveClinicJsonResponse> =
        hospitalAPI.getSelectiveClinic(sido = sido, sigungu = sigungu)

    override suspend fun getPolygonOsmId(adminArea: String): List<PolygonOsmId> =
        polygonAPI.getPolygonOsmId(city = adminArea)

    override suspend fun getPolygonData(osm_id: Long): Response<PolyGonResponse> =
        polygonAPI.getPolygonGeoJson(osmid = osm_id)
}