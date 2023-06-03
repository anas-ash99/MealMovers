package com.example.mealmoverskotlin.domain.usecases

import android.util.Log
import com.example.mealmoverskotlin.data.models.RestaurantHours
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class SetRestaurantHoursCase(private val hours: RestaurantHours){

    private lateinit var opensAt:LocalDateTime
    lateinit var closesAt :LocalDateTime
    init {
        setCompleteDates()
    }
    private fun setCompleteDates(){
        try {
            var day = ""

            day = if (LocalDateTime.now().monthValue < 10){

                LocalDateTime.now().year.toString() + "-0"  + LocalDateTime.now().monthValue.toString() + "-"  + LocalDateTime.now().dayOfMonth.toString()
            }else{
                LocalDateTime.now().year.toString() + "-"  + LocalDateTime.now().monthValue.toString() + "-"  + LocalDateTime.now().dayOfMonth.toString()
            }
            val pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            opensAt = LocalDateTime.parse( (day + " "  + hours.opens_at), pattern)
            closesAt =  LocalDateTime.parse( (day + " "  + hours.closes_at), pattern)

        }catch (e:Exception){
            Log.e("parse date", e.message, e)
        }
    }

    fun checkIfRestaurantIsOpen():Boolean{
        var isOpen = false
        try {

            if (LocalDateTime.now().isAfter(opensAt) && LocalDateTime.now().isBefore(closesAt)){
                isOpen = true
            }
        }catch (e:Exception){
            Log.e("parse date", e.message, e)
        }

        return isOpen
    }

     fun setTimeArray(): MutableList<String>{
         val timeArray = mutableListOf<String>()
//         timeArray.removeAll(timeArray)
         
         val min = if (!checkIfRestaurantIsOpen()){
             opensAt.hour
         }else{
             timeArray.add(0, "As soon as possible")
             LocalDateTime.now().hour + 1

         }
         val max = closesAt.hour - 1

        (min..max).onEach { hour ->
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