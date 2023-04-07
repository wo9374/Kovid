package com.ljb.data.local

import com.ljb.data.local.datasouce.LocalClinicSource
import com.ljb.data.mapper.mapperToClinic
import com.ljb.data.mapper.mapperToClinicJson
import com.ljb.data.mapper.mapperToSiDo
import com.ljb.domain.entity.Clinic
import com.ljb.domain.entity.SiDo
import com.ljb.domain.repository.LocalClinicRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * domain 레이어에 정의한 [LocalClinicRepository] 인터페이스의 구현 클래스
 * */
class LocalClinicRepositoryImpl @Inject constructor(
    private val localSource: LocalClinicSource
    ) : LocalClinicRepository {

    //선별 진료소 DB get
    override fun getLocalClinic(siDo: String, siGunGu: String, clinicType: Int): List<Clinic> {
        return localSource.getClinic(siDo, siGunGu, clinicType).map {
            it.mapperToClinic()
        }
    }

    override suspend fun insertClinic(clinic: Clinic, clinicType: Int) {
        val selectiveClinicJson = clinic.mapperToClinicJson(clinicType)
        localSource.insertClinic(selectiveClinicJson)
    }

    override suspend fun clearClinics() {
        localSource.clearClinics()
    }

    override fun mapInfoJsonParsing(jsonSido: String, jsonSiGungu: String) : Flow<List<SiDo>> {
        return flow {
            emit(
                localSource.mapInfoJsonParsing(jsonSido, jsonSiGungu).map {
                    it.mapperToSiDo()
                }
            )
        }
    }
}