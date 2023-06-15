package com.example.mealmoverskotlin.data.events

import com.example.mealmoverskotlin.data.models.MenuItemModel

sealed class CartEvent{
    data class AddItem(val item:MenuItemModel, val qut:Int): CartEvent()
    data class RemoveItem(val item: MenuItemModel, val qut:Int ):CartEvent()
}
