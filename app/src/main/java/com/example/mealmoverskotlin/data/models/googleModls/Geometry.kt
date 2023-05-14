package com.example.mealmoverskotlin.data.models.googleModls

data class Geometry(
    var bounds: Bounds = Bounds(),
    var location: Location = Location(),
    var location_type: String = "",
    var viewport: Viewport = Viewport()
)