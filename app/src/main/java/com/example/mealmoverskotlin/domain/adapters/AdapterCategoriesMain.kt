package com.example.mealmoverskotlin.domain.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mealmoverskotlin.R
import com.example.mealmoverskotlin.domain.viewModels.MainPageViewModel
import com.example.mealmoverskotlin.shared.Categories

class AdapterCategoriesMain(
    private val context: Context,
    currentCategory:Categories,
    private val onItemClick :(Categories) ->Unit

) : RecyclerView.Adapter<AdapterCategoriesMain.MyViewHolder>() {
//    private val items = arrayListOf<String>("All","Pizza", "Burger", "Sushi", "Döner")
    val items = arrayListOf(Categories.ALL, Categories.PIZZA, Categories.BURGER,Categories.SUSHI, Categories.DONER)
    var selectedPosition:Int = items.indexOf(currentCategory)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(R.layout.item_category_rv, parent,false)
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item:Categories= items[position]
        holder.nameTextView.text = item.value
        if (selectedPosition == position){
            holder.itemView.setBackgroundResource(R.drawable.category_item_selected)
        }else{
            holder.itemView.setBackgroundResource(R.drawable.category_item)
        }
        when(item){
            Categories.ALL-> holder.imageView.setBackgroundResource(R.drawable.food_location_icon)
            Categories.PIZZA -> holder.imageView.setBackgroundResource(R.drawable.pizza)
            Categories.SUSHI -> holder.imageView.setBackgroundResource(R.drawable.sushi_food_icon)
            Categories.DONER -> holder.imageView.setBackgroundResource(R.drawable.wrap_icon)
            Categories.BURGER -> holder.imageView.setBackgroundResource(R.drawable.burger_image)

        }
        holder.itemView.setOnClickListener {
            selectedPosition = position

            onItemClick(item)
            notifyDataSetChanged()
        }

    }



    override fun getItemCount(): Int {
        return items.size
    }

    class MyViewHolder(itemView:View): RecyclerView.ViewHolder(itemView){
         val nameTextView:TextView = itemView.findViewById(R.id.nameTextView)
        val imageView:ImageView = itemView.findViewById(R.id.imageView)

    }
}