package com.ljb.domain.entity

data class SelectiveClinic(
    val addr: String,               //주소                //경기도 하남시 신장2동 520
    val clinicName: String,         //선별 진료소명        //하남시보건소
    val create_dt: String,           //등록일자             //2022-04-06
    val disabledFacility: String,   //장애인시설           //""
    val gubun: String,              //진료소 구분          //null
    val satdayOperTime: String,     //토요일 운영시간       //09:00~17:00
    val satdayYn: String,           //토요일 운영여부       //Y
    val sido: String,               //시도명               //경기
    val sigungu: String,            //시군구명             //하남시
    val smpcolYn: String,           //검체 채취 가능여부    //Y
    val telNo: String,              //의료기관 연락처       //031-790-5917
    val weekOperTime: String,       //주중 운영시간         //09:00~17:00
    val weekYn: String,             //주중 운영여부         //Y
    val weekendOperTime: String,    //주말,공휴일 운영시간   //09:00~17:00
    val weekendYn: String,          //주말 공휴일 운영여부   //Y
)