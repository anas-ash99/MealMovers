package com.example.mealmoverskotlin.domain.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mealmoverskotlin.R
import com.example.mealmoverskotlin.data.models.OrderModel
import com.example.mealmoverskotlin.data.models.RestaurantModel
import com.example.mealmoverskotlin.ui.order.OrderActivity
import com.example.mealmoverskotlin.ui.order.OrdersHistoryActivity

class OrderHistoryAdapter(
    private val activity: OrdersHistoryActivity,
    private val orders: List<OrderModel>
) : RecyclerView.Adapter<OrderHistoryAdapter.MyViewHolder>() {




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(activity).inflate(R.layout.item_order_history,parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        var order =orders[position]

        holder.itemView.setOnClickListener {
            val intent = Intent(activity, OrderActivity::class.java)
            intent.putExtra( "order_id",order._id)
            intent.putExtra( "restaurantId",order.restaurant_id)
            activity.startActivity(intent)
        }


    }

    override fun getItemCount(): Int {
       return orders.size
    }



    inner class MyViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){

    }
}