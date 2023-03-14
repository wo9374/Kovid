package com.ljb.data.local.datasouce

import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import com.ljb.data.database.ClinicDao
import com.ljb.data.model.ClinicJson
import com.ljb.data.model.FeatureCollection
import javax.inject.Inject

interface LocalClinicSource {
    fun getClinic(siDo: String, siGunGu: String, clinicType: Int): List<ClinicJson>
    suspend fun insertClinic(clinicJson: ClinicJson)
    suspend fun clearClinics()

    fun mapInfoJsonParsing(jsonString: String) //: MapsInfo
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

    override fun mapInfoJsonParsing(jsonString: String) /*: MapsInfo*/ {
        val nogada = Gson().fromJson(jsonString, JsonObject::class.java)
        val featureObject = nogada.getAsJsonObject("features")

        Log.e("테스트 featureObject?.jsonArray", "${featureObject.asJsonArray}")
        Log.e("테스트", "${featureObject.asJsonArray.asJsonArray?.get(0)}")
        Log.e("테스트 type", "${featureObject.asJsonArray.asJsonArray?.get(0)?.asJsonObject?.get("type")}")
        Log.e("테스트 geometry", "${featureObject.asJsonArray.asJsonArray?.get(0)?.asJsonObject?.get("geometry")}")
        Log.e("테스트", "---1!----------------------------------------------------------------------------------------")
        Log.e("테스트 geometry type", "${featureObject.asJsonArray.asJsonArray?.get(0)?.asJsonObject?.get("geometry")?.asJsonObject?.get("type")}")
        Log.e("테스트 geometry coordinates", "${featureObject.asJsonArray.asJsonArray?.get(0)?.asJsonObject?.get("geometry")?.asJsonObject?.get("coordinates")}")
        Log.e("테스트", "---2!----------------------------------------------------------------------------------------")


        val data = Gson().fromJson(jsonString, object : TypeToken<FeatureCollection>(){}.type) as FeatureCollection
        data.features.forEach {
            Log.e("test", "data.type : ${data.type}, name : ${it.properties.ctpKorNm} polygons : ${it.geometry.coordinates}")
        }

        /*val siDo = Gson().fromJson(jsonString, object : TypeToken<MapJson>() {}.type) as MapJson
        val polygon = siDo.features[0].apply {
            properties
            geometry.type
            when(geometry.type){
                GeometryType.MultiPolygon -> (geometry.coordinates as Coordinate.MultiPolygonValue).value
                GeometryType.Polygon -> (geometry.coordinates as Coordinate.PolygonValue).value
            }
        }.mapperToKoreaPolygon()*/
    }
}