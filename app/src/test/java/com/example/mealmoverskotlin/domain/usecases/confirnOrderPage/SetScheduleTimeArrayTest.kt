package com.example.mealmoverskotlin.domain.usecases.confirnOrderPage

import android.annotation.SuppressLint
import com.example.mealmoverskotlin.data.models.RestaurantHours
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class SetScheduleTimeArrayTest{
    val setScheduleTimeArray = SetScheduleTimeArray()



    @Test
    fun `restaurant is closed`(){
        val result = setScheduleTimeArray.invoke(RestaurantHours("22:00:00", "23:00:00"), false)
        assertThat(result).isEqualTo(arrayListOf("22:00","22:15", "22:30", "22:45"))
    }


    @Test
    fun `restaurant is open`(){
        val result = setScheduleTimeArray.invoke(RestaurantHours("11:00:00", "18:00:00"), true)
        assertThat(result).isEqualTo(arrayListOf("As soon as possible", "16:45", "17:00","17:15", "17:30", "17:45"))
    }
}