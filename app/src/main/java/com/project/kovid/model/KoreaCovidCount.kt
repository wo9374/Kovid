package com.project.kovid.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class KoreaCovidCount {
    @SerializedName("resultCode")
    @Expose
    private val resultCode: String? = null

    @SerializedName("resultMessage")
    @Expose
    private val resultMessage: String? = null

    @SerializedName("TotalCase")
    @Expose
    private val totalCase: String? = null

    @SerializedName("TotalRecovered")
    @Expose
    private val totalRecovered: String? = null

    @SerializedName("TotalDeath")
    @Expose
    private val totalDeath: String? = null

    @SerializedName("NowCase")
    @Expose
    private val nowCase: String? = null

    @SerializedName("city1n")
    @Expose
    private val city1n: String? = null

    @SerializedName("city2n")
    @Expose
    private val city2n: String? = null

    @SerializedName("city3n")
    @Expose
    private val city3n: String? = null

    @SerializedName("city4n")
    @Expose
    private val city4n: String? = null

    @SerializedName("city5n")
    @Expose
    private val city5n: String? = null

    @SerializedName("city1p")
    @Expose
    private val city1p: String? = null

    @SerializedName("city2p")
    @Expose
    private val city2p: String? = null

    @SerializedName("city3p")
    @Expose
    private val city3p: String? = null

    @SerializedName("city4p")
    @Expose
    private val city4p: String? = null

    @SerializedName("city5p")
    @Expose
    private val city5p: String? = null

    @SerializedName("recoveredPercentage")
    @Expose
    private val recoveredPercentage: Double? = null

    @SerializedName("deathPercentage")
    @Expose
    private val deathPercentage: Double? = null

    @SerializedName("checkingCounter")
    @Expose
    private val checkingCounter: String? = null

    @SerializedName("checkingPercentage")
    @Expose
    private val checkingPercentage: String? = null

    @SerializedName("caseCount")
    @Expose
    private val caseCount: String? = null

    @SerializedName("casePercentage")
    @Expose
    private val casePercentage: String? = null

    @SerializedName("notcaseCount")
    @Expose
    private val notcaseCount: String? = null

    @SerializedName("notcasePercentage")
    @Expose
    private val notcasePercentage: String? = null

    @SerializedName("TotalChecking")
    @Expose
    private val totalChecking: String? = null

    @SerializedName("TodayRecovered")
    @Expose
    private val todayRecovered: String? = null

    @SerializedName("TodayDeath")
    @Expose
    private val todayDeath: String? = null

    @SerializedName("TotalCaseBefore")
    @Expose
    private val totalCaseBefore: String? = null

    @SerializedName("source")
    @Expose
    private val source: String? = null

    @SerializedName("updateTime")
    @Expose
    private val updateTime: String? = null
}