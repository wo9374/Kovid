package com.ljb.data.mapper

import android.location.Location
import com.ljb.data.model.SelectiveClinicJson
import com.ljb.data.model.SelectiveCluster
import com.ljb.domain.entity.SelectiveClinic

/**
 * domain layer는 data layer를 모르기 때문에 data에서 domain layer의 data class로 자료형을 변환
 * */

fun SelectiveClinicJson.mapperToSelective() = SelectiveClinic(
    addr = this.addr,
    clinicName = this.clinicName,
    create_dt = this.create_dt ?: "",
    disabledFacility= this.disabledFacility,
    gubun = this.gubun ?: "",
    satdayOperTime= this.satdayOperTime,
    satdayYn = this.satdayYn,
    sido = this.sido,
    sigungu = this.sigungu,
    smpcolYn = this.smpcolYn,
    telNo = this.telNo,
    weekOperTime = this.weekOperTime,
    weekYn = this.weekYn,
    weekendOperTime = this.weekendOperTime,
    weekendYn = this.weekendYn,
)

fun SelectiveClinic.mapperToSelectiveJson() = SelectiveClinicJson(
    addr = this.addr,
    clinicName = this.clinicName,
    create_dt = this.create_dt ?: "",
    disabledFacility= this.disabledFacility,
    gubun = this.gubun ?: "",
    satdayOperTime= this.satdayOperTime,
    satdayYn = this.satdayYn,
    sido = this.sido,
    sigungu = this.sigungu,
    smpcolYn = this.smpcolYn,
    telNo = this.telNo,
    weekOperTime = this.weekOperTime,
    weekYn = this.weekYn,
    weekendOperTime = this.weekendOperTime,
    weekendYn = this.weekendYn,
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
)

/*fun SelectiveClinicXml.mapperToSelective() = SelectiveClinic(
    addr = this.addr,
    clinicName = this.clinicName,
    crate_dt = this.crate_dt,
    disabledFacility= this.disabledFacility,
    gubun = this.gubun,
    satdayOperTime= this.satdayOperTime,
    satdayYn = this.satdayYn,
    sido = this.sido,
    sigungu = this.sigungu,
    smpcolYn = this.smpcolYn,
    telNo = this.telNo,
    weekOperTime = this.weekOperTime,
    weekYn = this.weekYn,
    weekendOperTime = this.weekendOperTime,
    weekendYn = this.weekendYn,
)*/

fun SelectiveCluster.latitude() = lat
fun SelectiveCluster.longitude() = lng