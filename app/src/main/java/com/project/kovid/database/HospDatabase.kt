package com.project.kovid.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.project.kovid.model.HospDBItem

@Database(entities = [HospDBItem::class], version = 1)
abstract class HospDatabase : RoomDatabase(){
    abstract fun hospDao() : HospDao

    companion object{
        private var Instance : HospDatabase? = null

        fun getInstance(context: Context): HospDatabase?{
            if (Instance == null) {
                synchronized(HospDatabase::class) { //synchronized: 여러 스레드가 동시에 접근 불가. 동기적으로 접근
                    Instance = Room.databaseBuilder(
                        context.applicationContext,
                        HospDatabase::class.java,
                        "HospDBItem.db")
                        .build()
                }
            }
            return Instance
        }
    }
}