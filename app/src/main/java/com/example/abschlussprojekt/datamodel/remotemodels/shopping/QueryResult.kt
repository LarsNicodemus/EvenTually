package com.example.abschlussprojekt.datamodel.remotemodels.shopping


data class QueryResult(val query: String, val result: Result<SearchResponse>)
