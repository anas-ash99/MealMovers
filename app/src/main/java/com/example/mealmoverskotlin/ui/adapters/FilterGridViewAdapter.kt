package com.example.mealmoverskotlin.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.example.mealmoverskotlin.R
import com.example.mealmoverskotlin.ui.dialogs.RestaurantsFilterDialog

class FilterGridViewAdapter(
    private val context: Context,
    private val dialog: RestaurantsFilterDialog,
    private var selectedItems2:MutableList<String>
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
    private var selectedItems:MutableList<String> = mutableListOf()
   init {
      selectedItems2.onEach {
          selectedItems.add(it)
      }

   }
    override fun getCount(): Int {
        return categoriesArray.size
    }

    override fun getItem(position: Int): Any? {
        return null
    }

    override fun getItemId(position: Int): Long {
        return 0
    }


    fun onClearFilterClick(){
        selectedItems.removeAll(selectedItems)
        notifyDataSetChanged()
    }
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var convertView = convertView
        if (layoutInflater == null) {
            layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        }

        if (convertView == null) {

            convertView = layoutInflater!!.inflate(R.layout.item_grid_filter, null)
        }

        val textView: TextView = convertView?.findViewById(R.id.textView)!!
        val card: CardView = convertView.findViewById(R.id.card_view)!!
        val item =  categoriesArray[position]
        textView.text = item

        card.setOnClickListener {

            if (!selectedItems.contains(item)!!){
                selectedItems.add(item)

            }else{
                selectedItems.remove(item)

            }
            notifyDataSetChanged()
            dialog.filterItems.value = selectedItems
        }


        if (selectedItems.contains(item)){
            card.setCardBackgroundColor(context.getColor(R.color.teal_200))
            textView.setTextColor(context.getColor(R.color.white))

        }else{
            card.setCardBackgroundColor(context.getColor(R.color.item_not_selected))
            textView.setTextColor(context.getColor(R.color.teal_200))
        }





        return convertView
    }
}