package com.ljb.data.remote.datasource

import com.ljb.data.di.NetworkModule
import com.ljb.data.model.PolygonOsmId
import com.ljb.data.model.PolyGonResponse
import com.ljb.data.remote.api.HospitalAPI
import com.ljb.data.model.SelectiveClinicJsonResponse
import retrofit2.Response
import javax.inject.Inject


interface RemoteClinicSource {
    suspend fun getSelectiveClinic(): Response<SelectiveClinicJsonResponse>
    suspend fun getTemporaryClinic(): Response<SelectiveClinicJsonResponse>
    suspend fun getPolygonOsmId(adminArea: String): List<PolygonOsmId>
    suspend fun getPolygonData(osm_id : Long): Response<PolyGonResponse>
}

class RemoteClinicSourceImpl @Inject constructor(
    @NetworkModule.HospitalType private val hospitalAPI: HospitalAPI,
    @NetworkModule.NominatimType private val polygonAPI: HospitalAPI,
) : RemoteClinicSource {
    override suspend fun getSelectiveClinic(): Response<SelectiveClinicJsonResponse> =
        hospitalAPI.getSelectiveClinic()

    override suspend fun getTemporaryClinic(): Response<SelectiveClinicJsonResponse> =
        hospitalAPI.getTemporaryClinic()

    override suspend fun getPolygonOsmId(adminArea: String): List<PolygonOsmId> =
        polygonAPI.getPolygonOsmId(city = adminArea)

    override suspend fun getPolygonData(osm_id: Long): Response<PolyGonResponse> =
        polygonAPI.getPolygonGeoJson(osmid = osm_id)
}