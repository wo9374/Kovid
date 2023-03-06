package com.ljb.data.mapper

import android.location.Location
import com.ljb.data.model.ClinicJson
import com.ljb.data.model.ClinicCluster
import com.ljb.domain.entity.Clinic

/**
 * domain layer는 data layer를 모르기 때문에 data에서 domain layer의 data class로 자료형을 변환
 * */

fun ClinicJson.mapperToClinic() = Clinic(
    sido = this.sido,
    sigungu = this.sigungu,
    clinicName = this.clinicName,
    addr = this.addr,
    telNo = this.telNo,
    smpcolYn = this.smpcolYn,
    weekYn = this.weekYn,
    satdayYn = this.satdayYn,
    weekendYn = this.weekendYn,
    weekOperTime = this.weekOperTime,
    satdayOperTime= this.satdayOperTime,
    weekendOperTime = this.weekendOperTime,

    clinicType = this.clinicType
)

fun Clinic.mapperToClinicJson(clinicType : Int) = ClinicJson(
    sido = this.sido,
    sigungu = this.sigungu,
    clinicName = this.clinicName,
    addr = this.addr,
    telNo = this.telNo,
    smpcolYn = this.smpcolYn,
    weekYn = this.weekYn,
    satdayYn = this.satdayYn,
    weekendYn = this.weekendYn,
    weekOperTime = this.weekOperTime,
    satdayOperTime= this.satdayOperTime,
    weekendOperTime = this.weekendOperTime,

    clinicType = clinicType
)

fun Clinic.mapperToCluster(location: Location) = ClinicCluster(
    addr = this.addr,
    clinicName = this.clinicName,
    satdayOperTime= this.satdayOperTime,
    satdayYn = this.satdayYn,
    sido = this.sido,
    sigungu = this.sigungu,
    telNo = this.telNo,
    weekOperTime = this.weekOperTime,
    weekYn = this.weekYn,
    weekendOperTime = this.weekendOperTime,
    weekendYn = this.weekendYn,

    lat = location.latitude,
    lng = location.longitude,

    clinicType = this.clinicType
)

fun ClinicCluster.latitude() = lat
fun ClinicCluster.longitude() = lng