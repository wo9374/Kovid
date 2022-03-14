package com.project.kovid.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.project.kovid.model.HospDBItem

@Dao
interface HospDao {
    @Query("SELECT * FROM HospDBItem")
    fun getAll(): LiveData<List<HospDBItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE) // 중복 ID일 경우 교체
    fun insert(hospDBItem: HospDBItem)

    @Update
    fun update(hospDBItem: HospDBItem)

    @Delete
    fun delete(hospDBItem: HospDBItem)
}