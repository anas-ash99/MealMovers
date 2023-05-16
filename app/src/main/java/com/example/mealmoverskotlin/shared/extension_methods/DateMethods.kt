package com.example.mealmoverskotlin.shared.extension_methods

import android.util.Log
import java.time.LocalDateTime
import java.time.format.DateTimeParseException

object DateMethods {


    fun String.getCurrentHour():String{

          var hour = ""
        try {
            val date = LocalDateTime.parse(this)
            hour = date.hour.toString()
        }catch (e:DateTimeParseException){
            Log.e("Date", e.message!!, e)
        }
        return hour

    }


}