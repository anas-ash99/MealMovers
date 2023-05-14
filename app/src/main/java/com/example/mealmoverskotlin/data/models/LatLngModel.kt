package com.example.mealmoverskotlin.data.models

import com.google.android.gms.maps.model.LatLng
import java.io.Serializable
data class  LatLngModel(
    var latitude:Double? = null,
    var longitude:Double? = null
):Serializable
