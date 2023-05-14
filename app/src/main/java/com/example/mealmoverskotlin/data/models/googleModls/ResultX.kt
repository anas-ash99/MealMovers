package com.example.mealmoverskotlin.data.models.googleModls

data class ResultX(
    var address_components: List<AddressComponentX> = listOf(),
    var formatted_address: String = "",
    var geometry: GeometryX = GeometryX(),
    var place_id: String = "",
    var plus_code: PlusCode = PlusCode(),
    var types: List<String> = listOf()
)