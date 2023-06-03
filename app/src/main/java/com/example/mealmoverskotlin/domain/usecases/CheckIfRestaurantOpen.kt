package com.example.mealmoverskotlin.domain.usecases

import android.util.Log
import com.example.mealmoverskotlin.data.models.RestaurantHours
import com.example.mealmoverskotlin.shared.extension_methods.DateMethods.getCompleteDateFromTime
import java.time.LocalDateTime

class CheckIfRestaurantOpen {

    fun invoke(hours: RestaurantHours):Boolean{
        var isOpen = false
        val opensAt =hours.opens_at.getCompleteDateFromTime()
        val closesAt = hours.closes_at.getCompleteDateFromTime()
        if (LocalDateTime.now().isAfter(opensAt) && LocalDateTime.now().isBefore(closesAt)){
            isOpen = true
        }

        println(opensAt)
        println(closesAt)
        return isOpen
    }


}