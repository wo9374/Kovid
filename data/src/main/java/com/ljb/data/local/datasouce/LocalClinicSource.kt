package com.ljb.data.local.datasouce

import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.ljb.data.database.ClinicDao
import com.ljb.data.mapper.parsingMapData
import com.ljb.data.model.ClinicJson
import com.ljb.data.model.SiDoModel
import com.ljb.domain.entity.SiDo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

interface LocalClinicSource {
    fun getClinic(siDo: String, siGunGu: String, clinicType: Int): List<ClinicJson>
    suspend fun insertClinic(clinicJson: ClinicJson)
    suspend fun clearClinics()

    suspend fun mapInfoJsonParsing(jsonSido: String, jsonSiGungu: String): Flow<List<SiDo>>
}

class LocalClinicSourceImpl @Inject constructor(private val clinicDao: ClinicDao) :
    LocalClinicSource {
    override fun getClinic(siDo: String, siGunGu: String, clinicType: Int): List<ClinicJson> {
        return if (siGunGu == "전체")
            clinicDao.getClinic(siDo, clinicType)
        else
            clinicDao.getClinicSiGunGu(siDo, siGunGu, clinicType)
    }

    override suspend fun insertClinic(clinicJson: ClinicJson) = clinicDao.insertClinic(clinicJson)

    override suspend fun clearClinics() = clinicDao.clearClinic()

    override suspend fun mapInfoJsonParsing(jsonSido: String, jsonSiGungu: String): Flow<List<SiDo>> {
        return flow {
            val siDoObject = Gson().fromJson(jsonSido, JsonObject::class.java)
            siDoObject.getAsJsonArray("features").mapIndexed { idx, element ->
                val siDoPolygon = element.parsingMapData(siDoType = true)

                val siGunGuObject = Gson().fromJson(jsonSiGungu, JsonObject::class.java)
                val parsingSiGun: List<SiDoModel.SiGunGuModel> =
                    siGunGuObject.getAsJsonArray("features").map { jsonElement ->
                        jsonElement.parsingMapData(false)
                    }

                val siGunList = mutableListOf<SiDoModel.SiGunGuModel>()
                when (idx) {
                    0 -> parsingSiGun.filterIndexedTo(siGunList) { index, _ -> index in 0..24 }  //서울 25 (구 25)
                    1 -> parsingSiGun.filterIndexedTo(siGunList) { index, _ -> index in 25..40 } //부산 16 (구 15 군 1)
                    2 -> parsingSiGun.filterIndexedTo(siGunList) { index, _ -> index in 41..48 } //대구 8  (구 7 군 1)
                    3 -> parsingSiGun.filterIndexedTo(siGunList) { index, _ -> index in 49..58 } //인천 10 (구 8 군 2)
                    4 -> parsingSiGun.filterIndexedTo(siGunList) { index, _ -> index in 59..63 } //광주 5  (구 5)
                    5 -> parsingSiGun.filterIndexedTo(siGunList) { index, _ -> index in 64..68 } //대전 5  (구 5)
                    6 -> parsingSiGun.filterIndexedTo(siGunList) { index, _ -> index in 69..73 } //울산 5  (구 4 군 1)
                    7 -> {} //세종 특별시 시군구 제외
                    8 -> parsingSiGun.filterIndexedTo(siGunList) { index, _ -> index in 74..104 } //경기도 (시 28 군 3)
                    9 -> parsingSiGun.filterIndexedTo(siGunList) { index, _ -> index in 105..122 } //강원도 (시 7 군 11)
                    10 -> parsingSiGun.filterIndexedTo(siGunList) { index, _ -> index in 123..133 } //충북 (시 3 군 8)
                    11 -> parsingSiGun.filterIndexedTo(siGunList) { index, _ -> index in 134..148 } //충남 (시 8 군 7)
                    12 -> parsingSiGun.filterIndexedTo(siGunList) { index, _ -> index in 149..162 } //전북 (시 6 군 8)
                    13 -> parsingSiGun.filterIndexedTo(siGunList) { index, _ -> index in 163..184 } //전남 (시 5 군 17)
                    14 -> parsingSiGun.filterIndexedTo(siGunList) { index, _ -> index in 185..207 } //경북 (시 10 군 13)
                    15 -> parsingSiGun.filterIndexedTo(siGunList) { index, _ -> index in 208..225 } //경남 (시 8 군 10)
                    16 -> parsingSiGun.filterIndexedTo(siGunList) { index, _ -> index in 226..227 } //제주 (시 2)
                }

                SiDoModel(
                    siDoPolygon.code,
                    siDoPolygon.name,
                    siDoPolygon.polygonType,
                    siDoPolygon.polygon,
                    siGunList
                )
            }
        }
    }
}