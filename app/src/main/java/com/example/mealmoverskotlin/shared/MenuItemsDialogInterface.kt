package com.example.mealmoverskotlin.shared

import com.example.mealmoverskotlin.data.models.MenuItemModel

interface MenuItemsDialogInterface {
    fun onPlusClick()
    fun onAddToCartClick(item:MenuItemModel)
}