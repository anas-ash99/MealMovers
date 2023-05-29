package com.example.mealmoverskotlin.ui.adapters

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
    val viewModel:OrderCheckoutPageViewModel
):RecyclerView.Adapter<DialogDeliveryTimeAdapter.MyViewHolder>() {
    lateinit var item:String
    private var selectedPosition:Int = viewModel.timeArray.indexOf(viewModel.order.deliveryTime)
    var selectedTime = viewModel.order.deliveryTime


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        return MyViewHolder(
            LayoutInflater.from(context).inflate(R.layout.item_delivery_time,parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        item  = viewModel.timeArray[position]
        holder.checkBox.isChecked = position == selectedPosition
        holder.checkBox.text = item
        holder.onClick()

    }

    override fun getItemCount(): Int {
        return viewModel.timeArray.size
    }

    inner class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val checkBox:CheckBox = itemView.findViewById(R.id.checkBox)

        fun onClick(){
            checkBox.setOnClickListener {
                selectedPosition = adapterPosition
                selectedTime = viewModel.timeArray[adapterPosition]
                notifyDataSetChanged()

            }
        }
    }
}