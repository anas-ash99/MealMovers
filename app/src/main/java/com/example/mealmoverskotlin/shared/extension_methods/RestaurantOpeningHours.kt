package com.example.mealmoverskotlin.shared.extension_methods

import com.example.mealmoverskotlin.data.models.RestaurantModel
import java.time.LocalDateTime

object RestaurantOpeningHours {

    fun RestaurantModel.checkIfOpen():Boolean{
        val opensAt = LocalDateTime.parse(this.opensAt)
        val closesAt = LocalDateTime.parse(this.closesAt)
        if (LocalDateTime.now().isAfter(opensAt) && !LocalDateTime.now().isAfter(closesAt)){
            return true
        }
        return false

    }
}