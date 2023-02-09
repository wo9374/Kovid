package com.ljb.domain.usecase

import com.ljb.domain.NetworkState
import com.ljb.domain.entity.SelectiveClinic
import com.ljb.domain.repository.HospitalRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSelectiveClinicUseCase @Inject constructor(
    private val repository: HospitalRepository
) {
    operator fun invoke(): Flow<NetworkState<List<SelectiveClinic>>> = repository.getSelectiveClinic()
}

class GetTemporaryClinicUseCase{

}