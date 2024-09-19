package com.example.abschlussprojekt.datamodel.remotemodels.news


data class NewsResponse(
    val status: String,
    val totalResults: Int,
    val articles: List<Article>
)
