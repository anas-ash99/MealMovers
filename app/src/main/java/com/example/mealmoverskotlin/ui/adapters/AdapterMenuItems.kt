package com.example.mealmoverskotlin.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mealmoverskotlin.R
import com.example.mealmoverskotlin.data.models.MenuItemModel
import com.example.mealmoverskotlin.domain.viewModels.RestaurantAndCartVM
import com.example.mealmoverskotlin.ui.dialogs.MenuItemDialog
import com.makeramen.roundedimageview.RoundedImageView

class AdapterMenuItems(
    private val context: Context,
    private val items:List<MenuItemModel>,
    private val onItemClick:(MenuItemModel)->Unit,
): RecyclerView.Adapter<AdapterMenuItems.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_menu,parent, false )
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item:MenuItemModel = items[position]

        holder.name.text = item.name
        holder.price.text = item.price + "€"
        if (item.imageUrl != "") {

            Glide.with(context).load(item.imageUrl).into(holder.itemImage)
        } else {
            holder.itemImage.visibility = View.GONE
        }

        holder.itemView.setOnClickListener {
            onItemClick(item)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val name: TextView = itemView.findViewById(R.id.name)
        val price: TextView = itemView.findViewById(R.id.price)
        val description: TextView = itemView.findViewById(R.id.description)
        val itemImage: RoundedImageView = itemView.findViewById(R.id.itemImage)

    }


}
