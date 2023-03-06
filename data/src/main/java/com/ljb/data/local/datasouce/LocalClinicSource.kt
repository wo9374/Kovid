package com.ljb.data.local.datasouce

import com.ljb.data.database.ClinicDao
import com.ljb.data.model.ClinicJson
import javax.inject.Inject

interface LocalClinicSource {
    fun getClinic(siDo: String, siGunGu: String, clinicType: Int): List<ClinicJson>
    suspend fun insertClinic(clinicJson: ClinicJson)
    suspend fun clearClinics()
}

class LocalClinicSourceImpl @Inject constructor(private val clinicDao: ClinicDao):
    LocalClinicSource {
    override fun getClinic(siDo : String, siGunGu: String, clinicType: Int): List<ClinicJson> {
        return if (siGunGu == "전체")
            clinicDao.getClinic(siDo, clinicType)
        else
            clinicDao.getClinicSiGunGu(siDo, siGunGu, clinicType)
    }

    override suspend fun insertClinic(clinicJson: ClinicJson) = clinicDao.insertClinic(clinicJson)

    override suspend fun clearClinics() = clinicDao.clearClinic()
}