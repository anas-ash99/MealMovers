package com.example.mealmoverskotlin.shared

import androidx.lifecycle.MutableLiveData
import com.example.mealmoverskotlin.data.models.AddressModel
import com.example.mealmoverskotlin.data.models.OrderModel
import com.example.mealmoverskotlin.data.models.RestaurantModel
import com.example.mealmoverskotlin.data.models.UserModel
import com.google.android.gms.maps.model.LatLng

object DataHolder {

    var order:OrderModel = OrderModel( address = AddressModel(city = "Bonn", zipCode = "53115", streetName = "Nidggerstr.", houseNumber = "12"))
    var restaurant:RestaurantModel = RestaurantModel()
    var loggedInUser:UserModel? = null
    var userAddress: AddressModel ? = null
    var myLatLng : MutableLiveData<LatLng?> = MutableLiveData()


    fun reinitOrderValues(){
        order = OrderModel(address = AddressModel(city = "Bonn", zipCode = "53115", streetName = "Nidggerstr.", houseNumber = "12"))

    }
}