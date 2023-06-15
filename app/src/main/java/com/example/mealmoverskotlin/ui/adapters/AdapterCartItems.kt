package com.example.mealmoverskotlin.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mealmoverskotlin.R
import com.example.mealmoverskotlin.data.models.CartItemModel
import com.example.mealmoverskotlin.data.models.MenuItemModel
import com.example.mealmoverskotlin.shared.CartItemClicksInterface
import com.makeramen.roundedimageview.RoundedImageView

class AdapterCartItems (
    private val context: Context,
    private val items:MutableList<CartItemModel>,
    private val onPlusClick:(CartItemModel)->Unit,
    private val onMinusClick:(CartItemModel)->Unit,
    ): RecyclerView.Adapter<AdapterCartItems.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(R.layout.item_cart_items,parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
       val item: CartItemModel = items[position]
        holder.itemPrice.text = "${item.item.price} €"
        holder.itemName.text = "${item.item.name} €"
        holder.itemQuantity.text = item.quantity.toString()
        if (item.item.imageUrl != ""){
            Glide.with(context).load(item.item.imageUrl).into(holder.itemImage)
        }
        holder.minusButton.setOnClickListener {

            onMinusClick(item)
//            holder.itemQuantity.text = item.quantity.toString()
        }
        holder.plusButton.setOnClickListener {

           onPlusClick(item)
//            holder.itemQuantity.text = item.quantity.toString()
        }

    }


    fun updateItem(position: Int){
        notifyItemChanged(position)
    }

    fun removeItem(position: Int){
        notifyItemRemoved(position)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val itemImage = itemView.findViewById<RoundedImageView>(R.id.itemImage)
        val itemQuantity: TextView = itemView.findViewById(R.id.itemCount)
        val itemName = itemView.findViewById<TextView>(R.id.itemName)
        val itemPrice = itemView.findViewById<TextView>(R.id.item_price)
        val minusButton:CardView = itemView.findViewById(R.id.minus_icon)
        val plusButton:CardView = itemView.findViewById(R.id.plus_icon)

    }
}
