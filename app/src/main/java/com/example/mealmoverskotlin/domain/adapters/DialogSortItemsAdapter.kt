package com.example.mealmoverskotlin.domain.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.mealmoverskotlin.R
import com.example.mealmoverskotlin.domain.dialogs.RestaurantsFilterDialog
import com.example.mealmoverskotlin.ui.mainPage.MainActivity


@SuppressLint("NotifyDataSetChanged")
class DialogSortItemsAdapter(
    private val activity: MainActivity,
    private val dialog: RestaurantsFilterDialog
):RecyclerView.Adapter<DialogSortItemsAdapter.MyViewHolder>() {


    var list = arrayOf("Recommended", "Delivery price", "Delivery time", "Rating", "distance")
    private var selectedPosition = 0



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(activity).inflate(R.layout.item_sort_filter_dialog,parent, false)
        )
    }


    override fun onBindViewHolder(holder: MyViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val item:String = list[position]
        holder.textView.text = item

        if (position == selectedPosition){
            holder.textView.setTextColor(activity.getColor(R.color.teal_200))
            holder.card.setCardBackgroundColor(activity.getColor(R.color.teal_200))

        }else{

            holder.textView.setTextColor(activity.getColor(R.color.text_color_sort_item))
            holder.card.setCardBackgroundColor(activity.getColor(R.color.secondary_background))
        }

        holder.card.setOnClickListener {
            selectedPosition = position
            notifyDataSetChanged()
            dialog.onSelectSortItem(item)
        }


    }

    fun onClearFilterClick(){
        selectedPosition = 0
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
         return list.size
    }



    inner class MyViewHolder(itemView:View) : RecyclerView.ViewHolder(itemView){
        val textView:TextView = itemView.findViewById(R.id.textView1)
        val card:CardView = itemView.findViewById(R.id.card)
    }
}