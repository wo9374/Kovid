package com.ljb.data.repository.remote.datasource

import com.ljb.data.remote.api.HospitalAPI
import com.ljb.data.remote.model.ClinicResponse
import retrofit2.Response
import javax.inject.Inject


interface HospitalDataSource {
    suspend fun getSelectiveClinic(): Response<ClinicResponse>
}

class HospitalDataSourceImpl @Inject constructor(
    private val hospitalAPI: HospitalAPI
) : HospitalDataSource {
    override suspend fun getSelectiveClinic(): Response<ClinicResponse> = hospitalAPI.getSelectiveClinic()
}
