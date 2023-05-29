package com.example.mealmoverskotlin.ui.dialogs

import android.content.Context
import android.view.Gravity
import androidx.cardview.widget.CardView
import com.example.mealmoverskotlin.R
import com.example.mealmoverskotlin.data.models.AddressModel
import com.example.mealmoverskotlin.domain.viewModels.OrderCheckoutPageViewModel
import com.example.mealmoverskotlin.shared.DataHolder
import com.google.android.material.textfield.TextInputEditText

class AddressFillingDialog(
    private val context:Context,
    private val viewModel: OrderCheckoutPageViewModel
): BaseDialog(context, Gravity.BOTTOM, R.layout.dialog_address_filling) {

  private val nameEditText:TextInputEditText = dialog.findViewById(R.id.name_edit_text)
  private val streetEditText:TextInputEditText = dialog.findViewById(R.id.street_edit_text)
  private val houseNumET:TextInputEditText = dialog.findViewById(R.id.houseNumET)
  private val zipCodeET:TextInputEditText = dialog.findViewById(R.id.ziCodeET)
  private val cityET:TextInputEditText = dialog.findViewById(R.id.cityET)
  private val phoneNumberET:TextInputEditText = dialog.findViewById(R.id.phone_numberET)
  private val instructionET:TextInputEditText = dialog.findViewById(R.id.instructionET)
  private val doneButton:CardView = dialog.findViewById(R.id.doneButtonAddress)
  private val hideDialogButton:CardView = dialog.findViewById(R.id.hideDialogButton)
  private var address:AddressModel? = DataHolder.userAddress

  init {

    if (address != null){

      nameEditText.setText(address?.name)
      streetEditText.setText(address?.streetName)
      houseNumET.setText(address?.houseNumber)
      zipCodeET.setText(address?.zipCode)
      cityET.setText(address?.city)
      phoneNumberET.setText(address?.phoneNumber)
      instructionET.setText(address?.instructions)
    }


    onHideClick()
    onDoneButtonClick()
  }

  private fun onDoneButtonClick(){

    doneButton.setOnClickListener {
      if (nameEditText.text?.toString()?.trim() == ""){
        nameEditText.error = "Filed is required"
      }
      if (streetEditText.text?.toString()?.trim() == ""){
        streetEditText.error = "Filed is required"
      }
      if (houseNumET.text?.toString()?.trim() == ""){
        houseNumET.error = "Filed is required"
      }
      if (cityET.text?.toString()?.trim() == ""){
        cityET.error = "Filed is required"
      }
      if (zipCodeET.text?.toString()?.trim() == ""){
        zipCodeET.error = "Filed is required"
      }
      if (phoneNumberET.text?.toString()?.trim() == ""){
        phoneNumberET.error = "Filed is required"
      }

      else{


        updateAddress()
        viewModel.binding.address = address
        viewModel.userAddress = address
        println(address)
        dialog.dismiss()
      }

    }

    println(cityET.text?.toString()?.trim()!!)
  }

  private fun updateAddress() {
    if (address== null){
       address = AddressModel()
      DataHolder.userAddress = address
    }

    address?.city = cityET.text?.toString()?.trim()!!
    address?.houseNumber = houseNumET.text?.toString()?.trim()!!
    address?.streetName = streetEditText.text?.toString()?.trim()!!
    address?.phoneNumber = phoneNumberET.text?.toString()?.trim()!!
    address?.zipCode = zipCodeET.text?.toString()?.trim()!!
    address?.instructions = instructionET.text?.toString()?.trim()!!
    address?.name = nameEditText.text?.toString()?.trim()!!


  }


  private fun onHideClick(){
    hideDialogButton.setOnClickListener {
      dialog.dismiss()
    }

  }









}