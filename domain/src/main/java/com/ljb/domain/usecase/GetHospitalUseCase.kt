package com.ljb.domain.usecase

import com.ljb.domain.NetworkState
import com.ljb.domain.entity.MapsPolygon
import com.ljb.domain.entity.Clinic
import com.ljb.domain.repository.LocalClinicRepository
import com.ljb.domain.repository.RemoteClinicRepository
import kotlinx.coroutines.flow.Flow

class GetMapsPolygonUseCase(private val repository: RemoteClinicRepository) {
    operator fun invoke(siDo: String, siGunGu: String): Flow<NetworkState<MapsPolygon>> =
        repository.getMapsPolygon(siDo, siGunGu)
}

class GetRemoteClinicUseCase(private val repository: RemoteClinicRepository) {
    operator fun invoke(clinicType: Int): Flow<NetworkState<List<Clinic>>> =
        repository.getRemoteClinic(clinicType)
}

class GetDbClinicUseCase(private val repository: LocalClinicRepository){
    operator fun invoke(siDo: String, siGunGu: String, clinicType :Int) : List<Clinic> =
        repository.getLocalClinic(siDo, siGunGu, clinicType)
}

class InsertSelectiveClinicUseCase(private val repository: LocalClinicRepository){
    suspend operator fun invoke(value : Clinic, clinicType: Int) = repository.insertClinic(value, clinicType)
}

class ClearSelectiveClinicUseCase(private val repository: LocalClinicRepository){
    suspend operator fun invoke() = repository.clearClinics()
}