package com.ljb.data.local

import com.ljb.data.mapper.mapperToSelective
import com.ljb.data.mapper.mapperToSelectiveJson
import com.ljb.data.local.datasouce.LocalClinicSource
import com.ljb.domain.entity.SelectiveClinic
import com.ljb.domain.repository.LocalClinicRepository
import javax.inject.Inject

/**
 * domain 레이어에 정의한 [LocalClinicRepository] 인터페이스의 구현 클래스
 * */
class LocalClinicRepositoryImpl @Inject constructor(private val localSource: LocalClinicSource) : LocalClinicRepository {

    //선별 진료소 DB get
    override fun getLocalClinic(sido: String, sigungu: String, clinicType: Int): List<SelectiveClinic> {
        return localSource.getClinic(sido, sigungu, clinicType).map {
            it.mapperToSelective()
        }
    }


    override suspend fun insertClinic(selectiveClinic: SelectiveClinic, clinicType: Int) {
        val selectiveClinicModel = selectiveClinic.mapperToSelectiveJson(clinicType)
        localSource.insertSelectiveClinic(selectiveClinicModel)
    }

    override suspend fun clearClinics() {
        localSource.clearSelectiveClinics()
    }
}