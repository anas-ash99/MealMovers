package com.example.mealmoverskotlin.shared.extension_methods

import android.util.Log
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

object DateMethods {


    fun String.getCurrentHourAndMinutes():String{
        var time = ""
        try {
            val date = LocalDateTime.parse(this)
            time = if (date.hour < 9){
                "0" + date.hour.toString() +":"+ date.minute.toString()
            }else{
                 date.hour.toString() +":"+ date.minute.toString()
            }
        }catch (e:DateTimeParseException){
            Log.e("Date", e.message!!, e)
        }
        return time
    }


    fun String.setOrderTime():String{
        var months = arrayOf(
            "January",
            "February",
            "March",
            "April",
            "May",
            "June",
            "July",
            "August",
            "September",
            "October",
            "November",
            "December"
        )

        var time = ""
        try {
            val date = LocalDateTime.parse(this)
            time = "${date.dayOfMonth} ${months[date.monthValue - 1]} ${date.year}, ${this.getCurrentHourAndMinutes()}"
        }catch (e:DateTimeParseException){
            Log.e("Date", e.message!!, e)
        }
        return time

    }

    fun String.parse():LocalDateTime{

        var date:LocalDateTime? = LocalDateTime.now()
        try {
           date = LocalDateTime.parse(this)
        }catch (e:DateTimeParseException){
            Log.e("Date", e.message!!, e)
        }
        return date!!

    }


    fun String.getCompleteDateFromTime():LocalDateTime?{
        var date:LocalDateTime? = null

        try {
            val pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            val day = LocalDateTime.now().toString().substring(0, LocalDateTime.now().toString().indexOf("T"))
            date = LocalDateTime.parse( ("$day $this"), pattern)
        }catch (e:Exception){
            Log.e("parse date", e.message, e)
        }

        return date
    }


}