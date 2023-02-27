package com.ljb.data.local.datasouce

import com.ljb.data.database.SelectiveClinicDao
import com.ljb.data.model.SelectiveClinicJson
import javax.inject.Inject

interface LocalClinicSource {
    fun getClinic(sido: String, sigungu: String, clinicType: Int): List<SelectiveClinicJson>
    suspend fun insertSelectiveClinic(selectiveCluster: SelectiveClinicJson)
    suspend fun clearSelectiveClinics()
}

class LocalClinicSourceImpl @Inject constructor(private val selectiveClinicDao: SelectiveClinicDao):
    LocalClinicSource {
    override fun getClinic(sido : String, sigungu: String, clinicType: Int): List<SelectiveClinicJson> {
        return if (sigungu.isEmpty() || sigungu == "전체")
            selectiveClinicDao.getClinic(sido, clinicType)
        else
            selectiveClinicDao.getClinicSigungu(sido, sigungu, clinicType)
    }

    override suspend fun insertSelectiveClinic(selectiveCluster: SelectiveClinicJson) {
        selectiveClinicDao.insertClinic(selectiveCluster)
    }

    override suspend fun clearSelectiveClinics() {
        selectiveClinicDao.clearSelectiveClinic()
    }
}