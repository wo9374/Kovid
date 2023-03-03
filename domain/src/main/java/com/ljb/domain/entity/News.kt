package com.ljb.domain.entity

data class News(
    val author: String,
    val content: String,
    val description: String,
    val publishedAt: String,
    //val newsSource: NewsSource,
    val title: String,
    val url: String,
    val urlToImage: String,
)