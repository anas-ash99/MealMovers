package com.example.mealmoverskotlin.data.models.googleModls

data class GeoResGoogle(
    var results: List<Result> = listOf(),
    var status: String = ""
)