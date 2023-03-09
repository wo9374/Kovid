package com.ljb.data.local.datasouce

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.ljb.data.database.ClinicDao
import com.ljb.data.mapper.mapperToMapsInfo
import com.ljb.data.mapper.safeCast
import com.ljb.data.model.ClinicJson
import com.ljb.data.model.GeometryType
import com.ljb.data.model.MapJson
import com.ljb.domain.entity.MapsInfo
import javax.inject.Inject

interface LocalClinicSource {
    fun getClinic(siDo: String, siGunGu: String, clinicType: Int): List<ClinicJson>
    suspend fun insertClinic(clinicJson: ClinicJson)
    suspend fun clearClinics()

    fun mapInfoJsonParsing(jsonString: String) : List<MapsInfo>
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

    override fun mapInfoJsonParsing(jsonString: String) : List<MapsInfo> {
        val siDo = Gson().fromJson(jsonString, object : TypeToken<MapJson>() {}.type) as MapJson
        siDo.features.map {
            when (it.geometry.type) {
                GeometryType.MultiPolygon -> it.geometry.coordinates.safeCast<List<List<List<Double>>>>()
                GeometryType.Polygon -> it.geometry.coordinates.safeCast<List<List<Double>>>()
            }
        }


        return siDo.mapperToMapsInfo()
    }
}