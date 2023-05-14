package com.example.mealmoverskotlin.shared

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

object KeyboardManger {

    fun Any.hideSoftKeyboard(view: View, context: Context) {
        val imm =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as
                    InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun Any.showSoftKeyboard(view: View, context: Context) {
        if (view.requestFocus()) {
            val imm: InputMethodManager =
                context.getSystemService(Context.INPUT_METHOD_SERVICE) as
                        InputMethodManager
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
        }
    }


}