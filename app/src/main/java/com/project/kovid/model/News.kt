package com.project.kovid.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.annotations.SerializedName

data class News(
    @SerializedName("articles")
    val articles: List<Article>,
    @SerializedName("status")
    val status: String,
    @SerializedName("totalResults")
    val totalResults: Int
) {
    data class Article(
        @SerializedName("author")
        val author: String,
        @SerializedName("content")
        val content: String,
        @SerializedName("description")
        val description: String,
        @SerializedName("publishedAt")
        val publishedAt: String,
        @SerializedName("source")
        val source: Source,
        @SerializedName("title")
        val title: String,
        @SerializedName("url")
        val url: String,
        @SerializedName("urlToImage")
        val urlToImage: String
    ) {
        data class Source(
            @SerializedName("id")
            val id: Any,
            @SerializedName("name")
            val name: String
        )
    }
}