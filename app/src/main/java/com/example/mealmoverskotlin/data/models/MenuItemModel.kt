package com.example.mealmoverskotlin.data.models

data class MenuItemModel (
    var _id:String = "",
    var name:String ="",
    var price:String = "",
    var imageUrl:String ="",
    var description:String = "",
    var quantity: Int = 1

    ):java.io.Serializable