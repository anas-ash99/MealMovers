package com.example.mealmoverskotlin.data.models

data class AddressModel(
    var name: String = "",
    var streetName: String = "",
    var houseNumber: String = "",
    var zipCode: String = "",
    var city: String = "",
    var address_line1:String ="",
    var address_line2:String = "",
    var instructions: String = "",
    var phoneNumber: String = "",
    var latitude:Double? = 0.0,
    var longitude:Double? = 0.0,
    val latlng:LatLngModel = LatLngModel()
):java.io.Serializable
