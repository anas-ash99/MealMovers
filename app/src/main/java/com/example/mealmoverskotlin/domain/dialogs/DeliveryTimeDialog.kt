package com.example.mealmoverskotlin.domain.dialogs

import android.content.Context
import android.view.Gravity
import androidx.cardview.widget.CardView
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mealmoverskotlin.R
import com.example.mealmoverskotlin.domain.adapters.DialogDeliveryTimeAdapter
import com.example.mealmoverskotlin.domain.viewModels.ConfirmOrderPageViewModel

class DeliveryTimeDialog (
    private val context: Context,
    private val viewModel: ConfirmOrderPageViewModel
) : BaseDialog(context, Gravity.CENTER,R.layout.dialog_chose_delivery_time) {

    private val adapter = DialogDeliveryTimeAdapter(context, viewModel)
    private val recyclerView:RecyclerView = dialog.findViewById(R.id.recyclerview)
    private val cancelButton:CardView = dialog.findViewById(R.id.cancel_button)
    private val selectButton:CardView = dialog.findViewById(R.id.select_button)
//    private val recyclerView:RecyclerView = dialog.findViewById(R.id.recyclerview)


      init {
          initRecyclerView()
          onButtonsClick()
      }

    private fun onButtonsClick() {
       selectButton.setOnClickListener {
           viewModel.onSelectDeliveryTime(adapter.selectedTime)
           dialog.dismiss()
       }
        cancelButton.setOnClickListener {
            dialog.dismiss()
        }
    }

    private fun initRecyclerView() {
       recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)
    }


}