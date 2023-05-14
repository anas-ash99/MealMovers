package com.example.mealmoverskotlin.data.models

data class RestaurantModel(
    var _id:String ="",
    var name:String ="",
    var deliveryTime:String ="",
    var deliveryPrice:String ="",
    var minOrderPrice:String ="",
    var image_url:String ="",
    var categories:MutableList<String> = mutableListOf(),
    var createdAt:String = "",
    var menu_items:List<MenuItemModel> = listOf(),
    var address: AddressModel? = AddressModel()
):java.io.Serializable
