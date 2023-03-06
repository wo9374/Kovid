package com.ljb.data.remote.datasource

import com.ljb.data.di.NetworkModule
import com.ljb.data.model.PolygonOsmId
import com.ljb.data.model.PolyGonResponse
import com.ljb.data.remote.api.HospitalAPI
import com.ljb.data.model.ClinicJsonResponse
import com.ljb.domain.entity.Clinic
import retrofit2.Response
import javax.inject.Inject


interface RemoteClinicSource {
    suspend fun getSelectiveClinic(clinicType : Int): Response<ClinicJsonResponse>
    suspend fun getPolygonOsmId(adminArea: String): List<PolygonOsmId>
    suspend fun getPolygonData(osm_id : Long): Response<PolyGonResponse>
}

class RemoteClinicSourceImpl @Inject constructor(
    @NetworkModule.HospitalType private val hospitalAPI: HospitalAPI,
    @NetworkModule.PolygonType private val polygonAPI: HospitalAPI,
) : RemoteClinicSource {
    override suspend fun getSelectiveClinic(clinicType : Int): Response<ClinicJsonResponse> = when(clinicType){
        Clinic.CLINIC_SELECTIVE ->
            hospitalAPI.getSelectiveClinic()
        else ->
            hospitalAPI.getTemporaryClinic() //Clinic.CLINIC_TEMPORARY
    }

    override suspend fun getPolygonOsmId(adminArea: String): List<PolygonOsmId> =
        polygonAPI.getPolygonOsmId(city = adminArea)

    override suspend fun getPolygonData(osm_id: Long): Response<PolyGonResponse> =
        polygonAPI.getPolygonGeoJson(osmid = osm_id)
}