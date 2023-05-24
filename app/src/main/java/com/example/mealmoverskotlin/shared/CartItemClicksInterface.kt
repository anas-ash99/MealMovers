package com.example.mealmoverskotlin.shared

import com.example.mealmoverskotlin.data.models.MenuItemModel

interface CartItemClicksInterface {
    fun onPlusClick(item:MenuItemModel)
    fun onMinusClick(item:MenuItemModel)
}