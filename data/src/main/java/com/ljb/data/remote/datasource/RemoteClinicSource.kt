package com.ljb.data.remote.datasource

import com.ljb.data.remote.api.HospitalAPI
import com.ljb.data.model.SelectiveClinicJsonResponse
import retrofit2.Response
import javax.inject.Inject


interface RemoteClinicSource {
    suspend fun getSelectiveClinic(sido:String, sigungu:String): Response<SelectiveClinicJsonResponse>
}

class RemoteClinicSourceImpl @Inject constructor(
    private val hospitalAPI: HospitalAPI
) : RemoteClinicSource {
    override suspend fun getSelectiveClinic(sido:String, sigungu:String): Response<SelectiveClinicJsonResponse> = hospitalAPI.getSelectiveClinic(sido = sido, sigungu = sigungu)
}