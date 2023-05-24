package com.example.mealmoverskotlin.domain

import com.example.mealmoverskotlin.data.models.OrderModel

interface RetrofitInterface {
    fun onSuccess(result:Any )
    fun onError(e:Exception)
}