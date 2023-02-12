package com.ljb.data.local.datasouce

import com.ljb.data.database.SelectiveClinicDao
import com.ljb.data.model.SelectiveClinicJson
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface LocalClinicSource {
    fun getSelectiveClinic(): List<SelectiveClinicJson>
    suspend fun insertSelectiveClinic(selectiveCluster: SelectiveClinicJson)
    suspend fun clearSelectiveClinics()
}

class LocalClinicSourceImpl @Inject constructor(private val selectiveClinicDao: SelectiveClinicDao):
    LocalClinicSource {
    override fun getSelectiveClinic(): List<SelectiveClinicJson> {
        return selectiveClinicDao.getSelectiveClinic()
    }

    override suspend fun insertSelectiveClinic(selectiveCluster: SelectiveClinicJson) {
        selectiveClinicDao.insertClinic(selectiveCluster)
    }

    override suspend fun clearSelectiveClinics() {
        selectiveClinicDao.clearSelectiveClinic()
    }
}