package com.project.kovid.function.repository

import android.app.Application
import com.project.kovid.BuildConfig
import com.project.kovid.RetrofitObject
import com.project.kovid.database.HospDao
import com.project.kovid.database.HospDatabase
import com.project.kovid.function.map.HospitalAPI
import com.project.kovid.model.HospDBItem
import com.project.kovid.model.HospResponse
import com.project.kovid.widget.util.TimeUtil
import retrofit2.Response

class MapRepository(application: Application) {
    private val hospitalRetrofit : retrofit2.Retrofit = RetrofitObject.getRetrofitHospital()
    private val hospitalApi = hospitalRetrofit.create(HospitalAPI::class.java)

    companion object{
        const val HOSP_COMPREHENSIVE = "11"
        const val HOSP_GENERAL = "21"
        const val HOSP_DOCTOR_OFFICE = "31"
    }
    suspend fun getSymptomTest(sido: String, sigungu: String) : Response<HospResponse> = hospitalApi.getHospital(
        serviceKey = BuildConfig.DATA_GO_KR_API_KEY_DECODE,
        pageNo = 1,
        numOfRows = 100,
        apiType = "json",
        create_dt = TimeUtil.getTodayDate(),
        sido = sido,
        sigungu = sigungu
    )

    private val hospDatabase: HospDatabase = HospDatabase.getInstance(application)!!
    private val hospDao: HospDao = hospDatabase.hospDao()

    fun getAll(): List<HospDBItem> {
        return hospDao.getAll()
    }

    fun insert(hospDBItem: HospDBItem){
        hospDao.insert(hospDBItem)
    }

    fun update(hospDBItem: HospDBItem){
        hospDao.update(hospDBItem)
    }
}