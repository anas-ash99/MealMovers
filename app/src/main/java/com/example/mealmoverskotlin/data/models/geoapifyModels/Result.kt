package com.example.mealmoverskotlin.data.models.geoapifyModels

data class Result(
    var address_line1: String? = "",
    var address_line2: String? = "",
    var housenumber: String? = "",
    var city: String? = "",
    var country: String? = "",
    var country_code: String? = "",
    var county: String? = "",
    var formatted: String? = "",
    var lat: Double? = null,
    var lon: Double? = null,
    var name: String? = "",
    var postcode: String? = "",
    var street: String? = "",

)