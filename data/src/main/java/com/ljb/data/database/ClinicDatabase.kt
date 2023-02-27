package com.ljb.data.database

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Insert
import androidx.room.Query
import androidx.room.RoomDatabase
import com.ljb.data.model.SelectiveClinicJson

@Database(entities = [SelectiveClinicJson::class], version = 1)
abstract class ClinicDatabase : RoomDatabase() {
    abstract fun dao(): SelectiveClinicDao
}

@Dao
interface SelectiveClinicDao{
    @Query("SELECT * From selective_clinic WHERE sido = :sido AND clinicType = :clinicType")
    fun getClinic(sido: String, clinicType: Int): List<SelectiveClinicJson>

    @Query("SELECT * From selective_clinic WHERE sido = :sido AND sigungu= :sigungu AND clinicType = :clinicType")
    fun getClinicSigungu(sido: String, sigungu: String, clinicType: Int): List<SelectiveClinicJson>

    @Insert
    suspend fun insertClinic(selectiveCluster: SelectiveClinicJson)

    @Query("DELETE FROM selective_clinic")
    suspend fun clearSelectiveClinic()
}