package com.example.mealmoverskotlin.ui.dialogs

import android.content.Context
import android.view.Gravity
import androidx.cardview.widget.CardView
import com.example.mealmoverskotlin.R
import javax.inject.Inject

class RestaurantClosedDialog (context: Context): BaseDialog(
    context,
    Gravity.CENTER,
    R.layout.restaurant_closed_dialog
) {
    val button: CardView = dialog.findViewById(R.id.button)
    init {
       onButtonClick()
    }

    private fun onButtonClick() {
        button.setOnClickListener {
            dialog.dismiss()
        }
    }

    fun showDialog(){
        dialog.show()
    }




}