package com.example.mealmoverskotlin.data.models.googleModls

data class GoogleLatlngResponse(
    var plus_code: PlusCode = PlusCode(),
    var results: List<ResultX> = listOf(),
    var status: String = ""
)