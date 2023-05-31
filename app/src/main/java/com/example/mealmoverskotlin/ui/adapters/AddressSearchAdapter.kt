package com.example.mealmoverskotlin.ui.adapters

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mealmoverskotlin.R
import com.example.mealmoverskotlin.data.models.AddressModel
import com.example.mealmoverskotlin.data.models.geoapifyModels.Result
import com.example.mealmoverskotlin.domain.viewModels.AddAddressViewModel
import com.google.android.libraries.places.api.model.AutocompletePrediction

class AddressSearchAdapter(
   private val activity: Activity,
   private val list:List<AutocompletePrediction>?,
   private val onAddressItemClick:(item:AddressModel) ->Unit
):RecyclerView.Adapter<AddressSearchAdapter.MyViewHolder>() {




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(activity).inflate(R.layout.item_street_name, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
         val item = list?.get(position)
         var line2 = item?.getFullText(null)?.substring(item.getFullText(null).indexOf(",") +2 )
          line2 = line2?.substring(0, line2.indexOf(","))

        holder.line1.text = item?.getPrimaryText(null)?.toString()!!
        holder.line2.text = line2
        holder.itemView.setOnClickListener {
            onAddressItemClick(convertAddress(item.getPrimaryText(null)?.toString()!!, line2!!))

        }

    }
    private fun convertAddress(line1:String, line2:String):AddressModel{

            val res = AddressModel( address_line1 = line1, address_line2 = line2 )
            if (line2.contains(" ")){
                res.zipCode = line2.substring(0, line2.indexOf(" "))
                res.city = line2.substring(line2.indexOf(" ") + 1)
            }else{
                res.city = line2
            }

            if(line1.contains(" ")){
                res.houseNumber = line1.substring(line1.indexOf(" ") + 1 )
                res.streetName = line1.substring(0, line1.indexOf(" "))
            }else{
                res.streetName = line1
            }
        return res
    }
    override fun getItemCount(): Int {
        return list?.size!!
    }
    inner class MyViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){
        val line1:TextView = itemView.findViewById(R.id.line1)
        val line2:TextView = itemView.findViewById(R.id.line2)

    }
}