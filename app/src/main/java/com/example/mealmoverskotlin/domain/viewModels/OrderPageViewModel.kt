package com.example.mealmoverskotlin.domain.viewModels

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mealmoverskotlin.R
import com.example.mealmoverskotlin.data.models.OrderModel
import com.example.mealmoverskotlin.data.models.RestaurantModel
import com.example.mealmoverskotlin.databinding.ActivityOrderBinding
import com.example.mealmoverskotlin.shared.RetrofitInterface
import com.example.mealmoverskotlin.domain.google.GoogleGeocoding
import com.example.mealmoverskotlin.domain.repositorylnterfaces.RestaurantRepositoryInterface
import com.example.mealmoverskotlin.ui.order.OrderActivity
import com.example.mealmoverskotlin.ui.order.TrackOrderFragment
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject



@HiltViewModel
class OrderPageViewModel @Inject constructor(
    private val context:Application,
    private val repository: RestaurantRepositoryInterface,
    private val googleGeocoding: GoogleGeocoding
): ViewModel() {

    @SuppressLint("StaticFieldLeak")
    private lateinit var activity: OrderActivity
    private lateinit var binding: ActivityOrderBinding

    var order:OrderModel? = null
    private var orderId:String?  = null
    private var restaurantId:String?  = null
    var userLatLng:LatLng? = null
    var restaurant:RestaurantModel? = null

    fun init(activity: OrderActivity, binding: ActivityOrderBinding){
        this.activity = activity
        this.binding = binding
//        getMap()

        binding.loading = true
        orderId = activity.intent.getStringExtra("order_id")
        restaurantId = activity.intent.getStringExtra("restaurantId")

        getOrder()
        getRestaurant()


    }




    private fun getOrder(){
        viewModelScope.launch {
            repository.getOrderById(orderId!!,object : RetrofitInterface {
                override fun onSuccess(res: Any) {
                    order = res as OrderModel
                    getLatlngOfOrder()
                }

                override fun onError(e: Exception) {
                    Toast.makeText(activity, e.message, Toast.LENGTH_SHORT).show()

                }
            } )
        }


    }




    private fun getLatlngOfOrder(){

        googleGeocoding.getAddress("${order?.address?.streetName} ${order?.address?.houseNumber}  ${order?.address?.zipCode}  ${order?.address?.city}"){ res, e ->

            e?.let {
                Log.e("get address", e.message, e)
                binding.loading = false
            }

            res?.let {
                if (it.results.isNotEmpty()){
                    userLatLng = LatLng(res.results[0].geometry.location.lat, res.results[0].geometry.location.lng)
                    startFragment()
                    binding.loading = false
                }
            }



        }

    }

    private fun startFragment(){

        if (restaurant != null && userLatLng != null){

            activity.supportFragmentManager.beginTransaction().apply {
                replace(R.id.fragment_layout, TrackOrderFragment(), "track_order")
                commit()
            }
        }

    }

    private fun getRestaurant(){
        viewModelScope.launch {
          repository.getRestaurantById(restaurantId!!, object : RetrofitInterface {
              override fun onSuccess(result: Any) {
                  restaurant = result as RestaurantModel
                  getLatlngOfOrder()
              }
              override fun onError(e: Exception) {
                  Toast.makeText(activity, e.message, Toast.LENGTH_SHORT).show()
              }
          })
        }
    }





}