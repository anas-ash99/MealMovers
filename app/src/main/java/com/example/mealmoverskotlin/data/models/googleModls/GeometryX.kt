package com.example.mealmoverskotlin.data.models.googleModls

data class GeometryX(
    var location: LocationX = LocationX(),
    var location_type: String = "",
    var viewport: ViewportX = ViewportX()
)