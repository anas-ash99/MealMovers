package com.example.mealmoverskotlin.data.models


data class OrderModel(
    var _id:String? =null,
    var restaurant_id: String = "",
    var restaurantName: String = "",
    var userId:String = "",
    var customerId:String = "",
    var created_at: String = "",
    var orderPrice: Double = 0.0,
    var itemsQuantity:Int = 0,
    var address: AddressModel = AddressModel(),
    var type: String = "",
    var status: String = "",
    var items: MutableList<MenuItemModel> = mutableListOf(),
    var paymentStatus: String = "",
    var ordered_at:String = "",
    var cart:MutableList<CartItemModel> = mutableListOf(),
    var deliveryTime: String = "",
    var isScheduled: Boolean = false
):java.io.Serializable


