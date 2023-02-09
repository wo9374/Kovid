package com.ljb.data.mapper

import com.google.android.gms.maps.model.LatLng
import com.ljb.data.remote.model.SelectiveCluster
import com.ljb.data.remote.model.SelectiveResponse
import com.ljb.domain.entity.SelectiveClinic

/**
 * domain layer는 data layer를 모르기 때문에 data에서 domain layer의 data class로 자료형을 변환
 * */

fun SelectiveClinic.mapperToCluster(latLng: LatLng) = SelectiveCluster(
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
    latLng = latLng
)

fun SelectiveResponse.mapperToSelective() = SelectiveClinic(
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
)

fun SelectiveClinic.mapperToSelectiveResponse() = SelectiveResponse(
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
)

fun SelectiveCluster.latitude() = latLng.latitude
fun SelectiveCluster.longitude() = latLng.longitude