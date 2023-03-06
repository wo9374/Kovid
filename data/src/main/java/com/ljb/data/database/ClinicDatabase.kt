package com.ljb.data.database

import androidx.room.*
import com.ljb.data.model.ClinicJson

@Database(entities = [ClinicJson::class], version = 1)
abstract class ClinicDatabase : RoomDatabase() {
    abstract fun dao(): ClinicDao
}

@Dao
interface ClinicDao{
    @Query("SELECT * From clinic WHERE sido = :siDo AND clinicType = :clinicType")
    fun getClinic(siDo: String, clinicType: Int): List<ClinicJson>

    @Query("SELECT * From clinic WHERE sido = :siDo AND sigungu= :siGunGu AND clinicType = :clinicType")
    fun getClinicSiGunGu(siDo: String, siGunGu: String, clinicType: Int): List<ClinicJson>

    @Insert
    suspend fun insertClinic(clinicJson: ClinicJson)

    @Query("DELETE FROM clinic")
    suspend fun clearClinic()
}