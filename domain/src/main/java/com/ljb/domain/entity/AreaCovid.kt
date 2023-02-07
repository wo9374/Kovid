package com.ljb.domain.entity

data class AreaCovid(
    var countryName : String,   //시도명(지역명)
    var newCase : String,       //신규확진환자수
    var totalCase : String,     //확진환자수
    var recovered : String,     //완치자수
    var death : String,         //사망자
    var percentage : String,    //발생률
    var newCcase : String,      //전일대비증감-해외유입
    var newFcase : String,      //전일대비증감-지역발생
)