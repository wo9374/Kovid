package com.ljb.data.local.datasouce

import com.ljb.data.database.SelectiveClinicDao
import com.ljb.data.model.SelectiveClinicJson
import javax.inject.Inject

interface LocalClinicSource {
    fun getClinic(siDo: String, siGunGu: String, clinicType: Int): List<SelectiveClinicJson>
    suspend fun insertSelectiveClinic(selectiveCluster: SelectiveClinicJson)
    suspend fun clearSelectiveClinics()
}

class LocalClinicSourceImpl @Inject constructor(private val selectiveClinicDao: SelectiveClinicDao):
    LocalClinicSource {
    override fun getClinic(siDo : String, siGunGu: String, clinicType: Int): List<SelectiveClinicJson> {
        return if (siGunGu == "전체")
            selectiveClinicDao.getClinic(siDo, clinicType)
        else
            selectiveClinicDao.getClinicSiGunGu(siDo, siGunGu, clinicType)
    }

    override suspend fun insertSelectiveClinic(selectiveClinicJson: SelectiveClinicJson) {
        selectiveClinicDao.insertClinic(selectiveClinicJson)
    }

    override suspend fun clearSelectiveClinics() {
        selectiveClinicDao.clearSelectiveClinic()
    }
}