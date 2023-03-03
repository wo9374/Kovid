package com.ljb.data.model

import com.google.gson.annotations.SerializedName

data class NewsResponse(
    @SerializedName("articles")
    val newsInfos: List<NewsBody>,
    @SerializedName("status")
    val status: String,
    @SerializedName("totalResults")
    val totalResults: Int
)

data class NewsBody(
    @SerializedName("author")
    val author: String?,
    @SerializedName("content")
    val content: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("publishedAt")
    val publishedAt: String,
    @SerializedName("source")
    val newsSource: NewsSource,
    @SerializedName("title")
    val title: String,
    @SerializedName("url")
    val url: String,
    @SerializedName("urlToImage")
    val urlToImage: String
)

data class NewsSource(
    @SerializedName("id")
    val id: Any,
    @SerializedName("name")
    val name: String
)

/*
data class NaverNews(
    @SerializedName("lastBuildDate") var lastBuildDate: String,
    @SerializedName("total") var total: Int,
    @SerializedName("start") var start: Int,
    @SerializedName("display") var display: Int,
    @SerializedName("items") var items: List<NaverNewsItems>
)

data class NaverNewsItems(
    @SerializedName("title") val title: String,
    @SerializedName("link") val link: String,
    @SerializedName("originallink") val originallink: String,
    @SerializedName("description") val description: String,
    @SerializedName("pubDate") val pubDate: String
)*/
