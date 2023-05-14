package com.example.mealmoverskotlin.data.models.googleModls

data class AddressComponent(
    var long_name: String = "",
    var short_name: String = "",
    var types: List<String> = listOf()
)