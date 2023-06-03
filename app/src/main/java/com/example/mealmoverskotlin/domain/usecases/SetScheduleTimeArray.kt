package com.example.mealmoverskotlin.domain.usecases

import com.example.mealmoverskotlin.data.models.RestaurantHours
import com.example.mealmoverskotlin.shared.extension_methods.DateMethods.getCompleteDateFromTime
import java.time.LocalDateTime

class SetScheduleTimeArray {


    fun invoke(hours: RestaurantHours, isOpen:Boolean): MutableList<String>{
        val timeArray = mutableListOf<String>()
        val opensAt = hours.opens_at.getCompleteDateFromTime()
        val closesAt = hours.closes_at.getCompleteDateFromTime()
        val min = if (!isOpen){
            opensAt?.hour
        }else{
            timeArray.add(0, "As soon as possible")
            LocalDateTime.now().hour + 1

        }
        val max = closesAt?.hour?.minus(1)

        (min!!..max!!).onEach { hour ->
            (0..60).onEach { minute->
                if (minute == 15 || minute == 30 || minute == 45 ) {
                    timeArray.add("$hour:$minute")
                }

                if (minute == 0){
                    timeArray.add("$hour:00")
                }

            }
        }
        return timeArray
    }
}