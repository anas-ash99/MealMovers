package com.example.mealmoverskotlin.ui.dialogs

import android.annotation.SuppressLint
import android.content.Context
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import com.bumptech.glide.Glide
import com.example.mealmoverskotlin.R
import com.example.mealmoverskotlin.data.events.CartEvent
import com.example.mealmoverskotlin.data.models.MenuItemModel
import com.example.mealmoverskotlin.domain.viewModels.RestaurantAndCartVM
import com.example.mealmoverskotlin.shared.MenuItemsDialogInterface
import com.example.mealmoverskotlin.shared.extension_methods.PriceTrimmer
import com.makeramen.roundedimageview.RoundedImageView
import java.text.DecimalFormat
@SuppressLint("SetTextI18n")
class MenuItemDialog(
    private val context: Context,
    private var cartEvent:MutableLiveData<CartEvent>,
): BaseDialog(context, Gravity.BOTTOM, R.layout.dialog_menu_item){

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

    private var itemQuantity = 1
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


    fun showDialog(item:MenuItemModel){
        Glide.with(context).load(item.imageUrl).into(itemImage)
        this.item = item
        itemQuantity = 1
        itemName.text = item.name
        itemPrice.text = item.price + "€"
        itemTotalPrice.text = "${item.price.toFloat() * item.quantity} €"
        quantity.text = itemQuantity.toString()
        dialog.show()
    }


    private fun onDoneClick(){
        addItemButton.setOnClickListener {
            cartEvent.value = CartEvent.AddItem(item, itemQuantity)
            dialog.dismiss()
        }

    }
    private fun onPlusClick() {

        addIcon.setOnClickListener {
            itemQuantity += 1
            quantity.text = itemQuantity.toString()
            itemTotalPrice.text = "${PriceTrimmer.trim(item.price.toDouble() * item.quantity)} €"

        }
    }

    private fun onMinusClick() {
        minusIcon.setOnClickListener {
            if (item.quantity > 1){
                itemQuantity -= 1
                quantity.text = itemQuantity.toString()
                itemTotalPrice.text = "${PriceTrimmer.trim(item.price.toDouble() * item.quantity)} €"

            }
        }

    }


}

