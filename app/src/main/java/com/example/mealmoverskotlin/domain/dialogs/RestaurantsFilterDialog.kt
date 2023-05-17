package com.example.mealmoverskotlin.domain.dialogs

import android.view.Gravity
import android.widget.GridView
import androidx.recyclerview.widget.RecyclerView
import com.example.mealmoverskotlin.R
import com.example.mealmoverskotlin.domain.adapters.FilterGridViewAdapter
import com.example.mealmoverskotlin.ui.mainPage.MainActivity

class RestaurantsFilterDialog(
    private val activity:MainActivity

):BaseDialog(activity, Gravity.BOTTOM, R.layout.dialog_filter_restaurants ) {

    private val gridViewAdapter = FilterGridViewAdapter(activity, this)
    private val gridView: GridView = dialog.findViewById<GridView>(R.id.gridView)
    private val recyclerView: RecyclerView = dialog.findViewById<RecyclerView>(R.id.RV_sortItems)

    init {
        initGridView()

    }

    private fun initGridView(){

         gridView.adapter = gridViewAdapter
    }









}