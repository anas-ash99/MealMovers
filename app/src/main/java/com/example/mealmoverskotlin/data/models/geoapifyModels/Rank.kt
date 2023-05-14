package com.example.mealmoverskotlin.data.models.geoapifyModels

data class Rank(
    var confidence: Int? = null,
    var importance: Double? = null,
    var match_type: String? = null,
    var popularity: Double? = null
)