package com.example.mealmoverskotlin.domain

import com.example.mealmoverskotlin.data.models.MenuItemModel

interface MenuItemsDialogInterface {
    fun onPlusClick()
    fun onAddToCartClick(item:MenuItemModel)
}