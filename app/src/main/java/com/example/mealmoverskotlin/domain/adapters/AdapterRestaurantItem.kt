package com.example.mealmoverskotlin.domain.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mealmoverskotlin.R
import com.example.mealmoverskotlin.data.models.RestaurantModel
import com.example.mealmoverskotlin.shared.DataHolder
import com.example.mealmoverskotlin.ui.restaurant_page.RestaurantActivity
import com.makeramen.roundedimageview.RoundedImageView

class AdapterRestaurantItem (
    private val context: Context,
    private var items:MutableList<RestaurantModel>,

        ): RecyclerView.Adapter<AdapterRestaurantItem.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
          LayoutInflater.from(context).inflate(R.layout.item_restaurant, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val restaurant:RestaurantModel = items[position]

        holder.resName.text = restaurant.name
        holder.delTime.text = restaurant.deliveryTime
        holder.delPrice.text = restaurant.deliveryPrice + "€"
        if (restaurant.image_url != "") {
            Glide.with(context).load(restaurant.image_url).into(holder.resImage)
        }
        holder.itemView.setOnClickListener {
              val intent = Intent(context, RestaurantActivity::class.java)
              intent.putExtra("RESTAURANT", restaurant)
              context.startActivity(intent)
            DataHolder.restaurant = restaurant
          }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class MyViewHolder(itemView:View): RecyclerView.ViewHolder(itemView){
        val delPrice = itemView.findViewById<TextView>(R.id.price)
         val delTime = itemView.findViewById<TextView>(R.id.time)
        val resName = itemView.findViewById<TextView>(R.id.name)
        val resImage = itemView.findViewById<RoundedImageView>(R.id.resImage)
    }
}
