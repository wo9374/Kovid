package com.ljb.data.database

import androidx.room.*
import com.ljb.data.model.SelectiveClinicJson

@Database(entities = [SelectiveClinicJson::class], version = 1)
abstract class ClinicDatabase : RoomDatabase() {
    abstract fun dao(): SelectiveClinicDao
}

@Dao
interface SelectiveClinicDao{
    @Query("SELECT * From selective_clinic WHERE sido = :siDo AND clinicType = :clinicType")
    fun getClinic(siDo: String, clinicType: Int): List<SelectiveClinicJson>

    @Query("SELECT * From selective_clinic WHERE sido = :siDo AND sigungu= :siGunGu AND clinicType = :clinicType")
    fun getClinicSiGunGu(siDo: String, siGunGu: String, clinicType: Int): List<SelectiveClinicJson>

    @Insert
    suspend fun insertClinic(selectiveClinicJson: SelectiveClinicJson)

    @Query("DELETE FROM selective_clinic")
    suspend fun clearSelectiveClinic()
}