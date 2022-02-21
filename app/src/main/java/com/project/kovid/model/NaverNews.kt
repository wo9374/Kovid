package com.project.kovid.model

import com.google.gson.annotations.SerializedName

data class NaverNews(
    @SerializedName("lastBuildDate") var lastBuildDate: String,
    @SerializedName("total") var total: Int,
    @SerializedName("start") var start: Int,
    @SerializedName("display") var display: Int,
    @SerializedName("items") var items: Array<Items>
) {
    data class Items(
        @SerializedName("title") val title: String,
        @SerializedName("link") val link: String,
        @SerializedName("originallink") val originallink: String,
        @SerializedName("description") val description: String,
        @SerializedName("pubDate") val pubDate: String
    )
}