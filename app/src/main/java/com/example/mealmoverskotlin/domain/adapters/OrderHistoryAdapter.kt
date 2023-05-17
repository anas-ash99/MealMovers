package com.example.mealmoverskotlin.domain.adapters

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mealmoverskotlin.R
import com.example.mealmoverskotlin.data.models.OrderModel
import com.example.mealmoverskotlin.shared.extension_methods.DateMethods.parse
import com.example.mealmoverskotlin.shared.extension_methods.DateMethods.setOrderTime
import com.example.mealmoverskotlin.shared.extension_methods.StringMethods.shortName
import com.example.mealmoverskotlin.ui.order.OrderActivity
import com.example.mealmoverskotlin.ui.order.OrdersHistoryActivity
import java.time.LocalDateTime

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
        val order =orders[position]
        val date =  "${order.created_at.parse().month} ${order.created_at.parse().year}"
        val orderStatus= "${order.status} · ${order.orderPrice}€"


        if (position == 0){
            holder.orderDate.visibility = View.VISIBLE

        }else{
            val prevOrder = orders[position  - 1]
            if ((prevOrder.created_at.parse().month != order.created_at.parse().month) || (prevOrder.created_at.parse().year != order.created_at.parse().year) ){
                holder.orderDate.visibility = View.VISIBLE
            }

        }

        holder.restaurantName.text = order.restaurantName
        holder.orderStatus.text = orderStatus
        holder.shortName.text = order.restaurantName.shortName()
        holder.orderDate.text = date
        holder.orderTime.text = order.created_at.setOrderTime()
        onItemClick(holder.orderItem, order)

    }


    private fun onItemClick(item:LinearLayout, order:OrderModel){
        item.setOnClickListener {
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
        val shortName:TextView = itemView.findViewById(R.id.resShortName)
        val orderTime:TextView = itemView.findViewById(R.id.orderTime)
        val orderItem:LinearLayout = itemView.findViewById(R.id.orderItem)
        val orderStatus:TextView = itemView.findViewById(R.id.orderStatus)
        val restaurantName:TextView = itemView.findViewById(R.id.restaurantName)
        val orderDate:TextView = itemView.findViewById(R.id.orderDate)
    }
}