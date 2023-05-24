package com.example.mealmoverskotlin.domain

import com.example.mealmoverskotlin.data.models.MenuItemModel

interface CartItemClicksInterface {
    fun onPlusClick(item:MenuItemModel)
    fun onMinusClick(item:MenuItemModel)
}