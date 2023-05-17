package com.example.mealmoverskotlin.domain.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.example.mealmoverskotlin.R
import com.example.mealmoverskotlin.domain.dialogs.RestaurantsFilterDialog

class FilterGridViewAdapter(
    private val context: Context,
    private val dialog:RestaurantsFilterDialog
): BaseAdapter() {
   private val categoriesArray = arrayOf(
        "Burger",
        "Pizza",
        "Salad",
        "Chicken",
        "Italian",
        "Sandwich",
        "Thai",
        "Wraps",
        "Asian",
        "Pasta",
        "Bowl",
        "Dessert",
        "American",
        "Curry",
        "Healthy",
        "Vegan",
        "Japanese",
        "Falafel",
        "Fish",
        "Indian",
        "Arab",
        "Noodles",
        "Gyros",
        "Cafe"
    )
    private var layoutInflater: LayoutInflater? = null
    override fun getCount(): Int {
        return categoriesArray.size
    }

    override fun getItem(position: Int): Any? {
        return null
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var convertView = convertView
        if (layoutInflater == null) {
            layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        }

        if (convertView == null) {

            convertView = layoutInflater!!.inflate(R.layout.item_grid_filter, null)
        }
        return convertView!!
    }
}