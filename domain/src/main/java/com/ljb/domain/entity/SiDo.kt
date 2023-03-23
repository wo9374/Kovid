package com.ljb.domain.entity

data class SiDo(
    val code : Int,
    val name : String,
    val polygonType : String,
    val polygon : List<List<List<Double>>>,
    val siGunList : List<SiGunGu>
){
    data class SiGunGu(
        val code : Int,
        val name : String,
        val polygonType : String,
        val polygon : List<List<List<Double>>>
    )
}