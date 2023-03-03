package com.ljb.data.mapper

import com.ljb.data.model.NewsBody
import com.ljb.data.util.removeHtmlTags
import com.ljb.domain.entity.News

fun NewsBody.mapperToNews() = News(
    author ?: "",
    content.removeHtmlTags(),
    description.removeHtmlTags(),
    publishedAt,
    title,
    url,
    urlToImage
)