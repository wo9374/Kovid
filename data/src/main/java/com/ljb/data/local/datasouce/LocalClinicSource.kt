package com.ljb.data.local.datasouce

import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.ljb.data.database.ClinicDao
import com.ljb.data.mapper.parsingMapData
import com.ljb.data.model.ClinicJson
import com.ljb.data.model.PolygonData.Companion.POLYGON
import com.ljb.data.model.PolygonInfo
import com.ljb.data.model.SiDoModel
import javax.inject.Inject

interface LocalClinicSource {
    fun getClinic(siDo: String, siGunGu: String, clinicType: Int): List<ClinicJson>
    suspend fun insertClinic(clinicJson: ClinicJson)
    suspend fun clearClinics()

    fun mapInfoJsonParsing(jsonSido: String, jsonSiGungu: String) //: MapsInfo
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

    override fun mapInfoJsonParsing(jsonSido: String, jsonSiGungu: String) /*: MapsInfo*/ {
        val siDoObject = Gson().fromJson(jsonSido, JsonObject::class.java)
        siDoObject.getAsJsonArray("features").forEachIndexed { idx, element ->
            val siDoPolygon = element.parsingMapData(siDoType = true)
            Log.e("parsedData", "$siDoPolygon")

            val siGunGuObject = Gson().fromJson(jsonSiGungu, JsonObject::class.java)
            val sigunList : List<PolygonInfo> = siGunGuObject.getAsJsonArray("features").map { jsonElement ->
                jsonElement.parsingMapData(false)
            }

            val temp = mutableListOf<PolygonInfo>()
            when(idx){
                0 -> sigunList.filterIndexedTo(temp){ index, _ -> index in 0..24 }  //서울 25 (구 25)
                1 -> sigunList.filterIndexedTo(temp){ index, _ -> index in 25..40 } //부산 16 (구 15 군 1)
                2 -> sigunList.filterIndexedTo(temp){ index, _ -> index in 41..48 } //대구 8  (구 7 군 1)
                3 -> sigunList.filterIndexedTo(temp){ index, _ -> index in 49..58 } //인천 10 (구 8 군 2)
                4 -> sigunList.filterIndexedTo(temp){ index, _ -> index in 59..63 } //광주 5  (구 5)
                5 -> sigunList.filterIndexedTo(temp){ index, _ -> index in 64..68 } //대전 5  (구 5)
                6 -> sigunList.filterIndexedTo(temp){ index, _ -> index in 69..73 } //울산 5  (구 4 군 1)
            }

            Log.e("시군구", "$temp")
        }
    }
}