package com.example.mealmoverskotlin.data.models

data class CartItemModel(
    val item:MenuItemModel = MenuItemModel(),
    var quantity:Int = 1
):java.io.Serializable
