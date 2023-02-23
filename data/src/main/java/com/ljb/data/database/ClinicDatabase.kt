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
    @Query("SELECT * From selective_clinic WHERE sido = :sido")
    fun getSelectiveClinic(sido: String): List<SelectiveClinicJson>

    @Query("SELECT * From selective_clinic WHERE sido = :sido AND sigungu= :sigungu")
    fun getSelectiveClinicSigungu(sido: String, sigungu: String): List<SelectiveClinicJson>

    @Insert
    suspend fun insertClinic(selectiveCluster: SelectiveClinicJson)

    @Query("DELETE FROM selective_clinic")
    suspend fun clearSelectiveClinic()
}