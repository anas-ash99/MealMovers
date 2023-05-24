package com.example.mealmoverskotlin.domain.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mealmoverskotlin.R
import com.example.mealmoverskotlin.data.models.MenuItemModel

class AdapterOrderItem(
    private val context: Context,
    private val items:List<MenuItemModel>
): RecyclerView.Adapter<AdapterOrderItem.MyViewHolder>() {




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(R.layout.item_ordered_items, parent, false)
        )
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = items[position]

        holder.itemPrice.text = item.price + "€"
        holder.itemName.text = item.name
        holder.itemQuantity.text = "${item.quantity}x"
    }

    override fun getItemCount(): Int {
        return items.size
    }
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var itemQuantity: TextView
        var itemPrice: TextView
        var itemName: TextView

        init {
            itemQuantity = itemView.findViewById(R.id.item_quantity)
            itemName = itemView.findViewById(R.id.itemName)
            itemPrice = itemView.findViewById(R.id.itemPrice)
        }
    }


}