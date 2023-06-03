package com.example.mealmoverskotlin.ui.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView
import com.example.mealmoverskotlin.R
import com.example.mealmoverskotlin.domain.viewModels.OrderCheckoutPageViewModel

class DialogDeliveryTimeAdapter(
    private val context: Context,
    private val onItemClick:(String)->Unit,
    private val timeArray: List<String>,
    private var selectedTime:String,

):RecyclerView.Adapter<DialogDeliveryTimeAdapter.MyViewHolder>() {
    lateinit var item:String
    private var selectedPosition:Int = timeArray.indexOf(selectedTime)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        return MyViewHolder(
            LayoutInflater.from(context).inflate(R.layout.item_delivery_time,parent, false)
        )
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: MyViewHolder, @SuppressLint("RecyclerView") position: Int) {
        item  = timeArray[position]
        holder.checkBox.isChecked = position == selectedPosition
        holder.checkBox.text = item
//        holder.checkBox.setOnClickListener {
//            selectedPosition = position
//            selectedTime = timeArray[position]
//            onItemClick(item)
//            notifyDataSetChanged()
//
//        }

    }

    override fun getItemCount(): Int {
        return timeArray.size
    }

    inner class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val checkBox:CheckBox = itemView.findViewById(R.id.checkBox)
          init {
              onClick()
          }
        private fun onClick(){
            checkBox.setOnClickListener {
                selectedPosition = adapterPosition
                selectedTime = timeArray[adapterPosition]
                onItemClick(timeArray[adapterPosition])
                notifyDataSetChanged()

            }
        }
    }
}