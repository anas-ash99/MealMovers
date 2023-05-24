package com.example.mealmoverskotlin.shared

import com.example.mealmoverskotlin.data.models.OrderModel

interface RetrofitInterface {
    fun onSuccess(result:Any )
    fun onError(e:Exception)
}