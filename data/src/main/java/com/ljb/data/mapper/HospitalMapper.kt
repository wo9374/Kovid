package com.ljb.data.mapper

import android.location.Location
import com.ljb.data.model.SelectiveClinicJson
import com.ljb.data.model.SelectiveCluster
import com.ljb.domain.entity.SelectiveClinic

/**
 * domain layer는 data layer를 모르기 때문에 data에서 domain layer의 data class로 자료형을 변환
 * */

fun SelectiveClinicJson.mapperToSelective() = SelectiveClinic(
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

fun SelectiveClinic.mapperToSelectiveJson(clinicType : Int) = SelectiveClinicJson(
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

fun SelectiveClinic.mapperToCluster(location: Location) = SelectiveCluster(
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

fun SelectiveCluster.latitude() = lat
fun SelectiveCluster.longitude() = lng