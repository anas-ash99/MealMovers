package com.example.mealmoverskotlin.domain.dialogs

import android.content.Context
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.mealmoverskotlin.R
import com.example.mealmoverskotlin.data.models.MenuItemModel
import com.example.mealmoverskotlin.domain.MenuItemsDialogInterface
import com.example.mealmoverskotlin.shared.PriceTrimmer
import com.makeramen.roundedimageview.RoundedImageView
import java.text.DecimalFormat

class MenuItemDialog(
    private val context: Context,
    private val interfaceClicks:MenuItemsDialogInterface
):BaseDialog(context, Gravity.BOTTOM, R.layout.dialog_menu_item){

    var item:MenuItemModel = MenuItemModel()
    private var itemName:TextView = dialog.findViewById(R.id.itemName)
    private var itemPrice:TextView = dialog.findViewById(R.id.item_price)
    private var itemTotalPrice:TextView = dialog.findViewById(R.id.item_final_price)
    private var quantity:TextView = dialog.findViewById(R.id.items_quantity)
    private var addIcon:LinearLayout = dialog.findViewById(R.id.plus_icon)
    private var minusIcon:LinearLayout = dialog.findViewById(R.id.minus_icon)
    private var hideIcon:LinearLayout = dialog.findViewById(R.id.hideIcon)
    private var addItemButton:LinearLayout = dialog.findViewById(R.id.addToCartButton)
    private var itemImage:RoundedImageView = dialog.findViewById(R.id.itemImage)

    init {
        onPlusClick()
        onMinusClick()
        onDoneClick()
        onHideIconClick()
    }

    private fun onHideIconClick() {
        hideIcon.setOnClickListener {
            dialog.dismiss()
        }
    }

    fun showDialog(){
        Glide.with(context).load(item.imageUrl).into(itemImage)
        itemName.text = item.name
        itemPrice.text = item.price + "€"
        itemTotalPrice.text = "${item.price.toFloat() * item.quantity} €"
        quantity.text = item.quantity.toString()
        dialog.show()
    }


    private fun onDoneClick(){
        addItemButton.setOnClickListener {
            interfaceClicks.onAddToCartClick(item)
            dialog.dismiss()
        }

    }
    private fun onPlusClick() {
        val dc = DecimalFormat()
        dc.maximumFractionDigits = 2
        addIcon.setOnClickListener {
            item.quantity += 1
            quantity.text = item.quantity.toString()
            itemTotalPrice.text = "${PriceTrimmer.trim(item.price.toDouble() * item.quantity)} €"
            interfaceClicks.onPlusClick()
        }
    }

    private fun onMinusClick() {
        val dc = DecimalFormat()
        dc.maximumFractionDigits = 2
        minusIcon.setOnClickListener {
            if (item.quantity > 1){
                item.quantity -= 1
                quantity.text = item.quantity.toString()
                itemTotalPrice.text = "${PriceTrimmer.trim(item.price.toDouble() * item.quantity)} €"
                interfaceClicks.onPlusClick()

            }
        }

    }


}

