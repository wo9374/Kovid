package com.ljb.domain.usecase

import com.ljb.domain.NetworkState
import com.ljb.domain.entity.MapsPolygon
import com.ljb.domain.entity.SelectiveClinic
import com.ljb.domain.repository.LocalClinicRepository
import com.ljb.domain.repository.RemoteClinicRepository
import kotlinx.coroutines.flow.Flow

class GetMapsPolygonUseCase(private val repository: RemoteClinicRepository) {
    operator fun invoke(siDo: String, siGunGu: String): Flow<NetworkState<MapsPolygon>> =
        repository.getMapsPolygon(siDo, siGunGu)
}

class GetSelectiveClinicUseCase(private val repository: RemoteClinicRepository) {
    operator fun invoke(): Flow<NetworkState<List<SelectiveClinic>>> =
        repository.getRemoteSelectiveClinic()
}

class GetTemporaryClinicUseCase(private val repository: RemoteClinicRepository){
    operator fun invoke(): Flow<NetworkState<List<SelectiveClinic>>> =
        repository.getRemoteTemporaryClinic()
}

class GetDbClinicUseCase(private val repository: LocalClinicRepository){
    operator fun invoke(siDo: String, siGunGu: String, clinicType :Int) : List<SelectiveClinic> =
        repository.getLocalClinic(siDo, siGunGu, clinicType)
}

class InsertSelectiveClinicUseCase(private val repository: LocalClinicRepository){
    suspend operator fun invoke(value : SelectiveClinic, clinicType: Int) = repository.insertClinic(value, clinicType)
}

class ClearSelectiveClinicUseCase(private val repository: LocalClinicRepository){
    suspend operator fun invoke() = repository.clearClinics()
}