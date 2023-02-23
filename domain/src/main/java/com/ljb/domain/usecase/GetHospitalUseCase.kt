package com.ljb.domain.usecase

import com.ljb.domain.NetworkState
import com.ljb.domain.entity.MapsPolygon
import com.ljb.domain.entity.SelectiveClinic
import com.ljb.domain.repository.LocalClinicRepository
import com.ljb.domain.repository.RemoteClinicRepository
import kotlinx.coroutines.flow.Flow

class GetMapsPolygonUseCase(private val repository: RemoteClinicRepository) {
    operator fun invoke(sido: String, sigungu: String): Flow<NetworkState<MapsPolygon>> =
        repository.getMapsPolygon(sido, sigungu)
}

class GetSelectiveClinicUseCase(private val repository: RemoteClinicRepository) {
    operator fun invoke(sido:String): Flow<NetworkState<List<SelectiveClinic>>> =
        repository.getRemoteSelectiveClinic(sido)
}

class GetDbSelectiveClinicUseCase(private val repository: LocalClinicRepository){
    operator fun invoke(sido: String, sigungu: String) : List<SelectiveClinic> = repository.getLocalSelectiveClinic(sido, sigungu)
}

class InsertSelectiveClinicUseCase(private val repository: LocalClinicRepository){
    suspend operator fun invoke(value : SelectiveClinic) = repository.insertSelectiveClinic(value)
}

class ClearSelectiveClinicUseCase(private val repository: LocalClinicRepository){
    suspend operator fun invoke() = repository.clearSelectiveClinics()
}