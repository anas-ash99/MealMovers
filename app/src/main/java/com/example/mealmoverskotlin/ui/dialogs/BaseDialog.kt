package com.example.mealmoverskotlin.ui.dialogs

import android.app.Dialog
import android.content.Context
import android.view.ViewGroup
import android.view.Window
import com.example.mealmoverskotlin.R

open class BaseDialog (
    context: Context,
    gravity: Int,
    layout:Int
        ) {

    val dialog = Dialog(context)

    init {
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog?.setContentView(layout)
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog?.window?.attributes?.windowAnimations = R.style.DialogAnimation
        dialog?.window?.setGravity(gravity)

    }
}