package com.example.mealmoverskotlin.ui.dialogs

import android.content.Context
import android.view.Gravity
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mealmoverskotlin.R
import com.example.mealmoverskotlin.ui.adapters.DialogDeliveryTimeAdapter
import com.example.mealmoverskotlin.domain.viewModels.OrderCheckoutPageViewModel

class DeliveryTimeDialog (
    private val context: Context,
    private val viewModel: OrderCheckoutPageViewModel
) : BaseDialog(context, Gravity.CENTER,R.layout.dialog_chose_delivery_time) {

    private var adapter:DialogDeliveryTimeAdapter? = null
    private val recyclerView:RecyclerView = dialog.findViewById(R.id.recyclerview)
    private val cancelButton:CardView = dialog.findViewById(R.id.cancel_button)
    private val selectButton:CardView = dialog.findViewById(R.id.select_button)
    private var selectedTime = ""

    init {
        onButtonsClick()
    }
    fun showDialog(){
        selectedTime = if (selectedTime == ""){
            viewModel.timeArray[0]

        }else{
            viewModel.order.deliveryTime
        }
        initRecyclerView()
        dialog.show()
    }

    private fun onItemClick(item:String){
       selectedTime = item
    }
    private fun onButtonsClick() {
       selectButton.setOnClickListener {
           viewModel.order.deliveryTime = selectedTime
           viewModel.deliveryTime.value = selectedTime
           dialog.dismiss()
       }
        cancelButton.setOnClickListener {
            dialog.dismiss()
        }
    }

    private fun initRecyclerView() {
        adapter = DialogDeliveryTimeAdapter(context,::onItemClick, viewModel.timeArray,selectedTime)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.scrollToPosition(viewModel.timeArray.indexOf(selectedTime))
    }


}