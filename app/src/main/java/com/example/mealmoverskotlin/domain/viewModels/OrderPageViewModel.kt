package com.example.mealmoverskotlin.domain.viewModels

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mealmoverskotlin.R
import com.example.mealmoverskotlin.data.models.LatLngModel
import com.example.mealmoverskotlin.data.models.OrderModel
import com.example.mealmoverskotlin.data.models.RestaurantModel
import com.example.mealmoverskotlin.data.models.geoapifyModels.GeoapifyModel
import com.example.mealmoverskotlin.data.models.googleModls.GeoResGoogle
import com.example.mealmoverskotlin.databinding.ActivityOrderBinding
import com.example.mealmoverskotlin.domain.RetrofitInterface
import com.example.mealmoverskotlin.domain.geoapify.AutoCompleteAddress
import com.example.mealmoverskotlin.domain.geoapify.Geoapify
import com.example.mealmoverskotlin.domain.google.GoogleGeocoding
import com.example.mealmoverskotlin.domain.google.OnDone
import com.example.mealmoverskotlin.domain.repositorylnterfaces.MainRepositoryInterface
import com.example.mealmoverskotlin.ui.order.OrderActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject



@HiltViewModel
class OrderPageViewModel @Inject constructor(
    private val repository: MainRepositoryInterface
): ViewModel() {
    var map:MutableLiveData<GoogleMap?> = MutableLiveData(null)
    private lateinit var activity: OrderActivity
    private lateinit var binding: ActivityOrderBinding
    private lateinit var geoapify: Geoapify
    private lateinit var googleGeocoding: GoogleGeocoding
    private var order:OrderModel? = null
    private var orderId:String?  = null
    private var restaurantId:String?  = null
    private var userLatLng:LatLng? = null
    private var restaurant:RestaurantModel? = null

    fun init(activity: OrderActivity, binding: ActivityOrderBinding){
        this.activity = activity
        this.binding = binding
        getMap()
        googleGeocoding = GoogleGeocoding(activity)
        binding.loading = true
        geoapify = Geoapify(activity)
        orderId = activity.intent.getStringExtra("order_id")
        restaurantId = activity.intent.getStringExtra("restaurantId")
        getOrder()
        getRestaurant()

    }



    private fun getMap() {

        try {
            val mapFragment: SupportMapFragment = activity.supportFragmentManager.findFragmentById(R.id.mapView) as SupportMapFragment
            mapFragment.getMapAsync {
                  map.value = it
            }
        }catch (e:Exception){
            Log.e("Map", e.message!!, e)
            Toast.makeText(activity, "Something went wrong initializing the map", Toast.LENGTH_SHORT).show()
        }
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

        googleGeocoding.getAddress("${order?.address?.streetName} ${order?.address?.houseNumber}  ${order?.address?.zipCode}  ${order?.address?.city}", object : OnDone {
            override fun onLoadingDone(res1: Any?) {
                val res:GeoResGoogle = res1  as GeoResGoogle
                userLatLng = LatLng(res.results[0].geometry.location.lat, res.results[0].geometry.location.lng)
                if (map.value != null && restaurant != null){
                    initMap()
                }
                binding.loading = false
            }

            override fun onError(e: Exception) {
                Log.e("get address", e.message, e)
                binding.loading = false
            }
        })

    }

    fun initMap(){

        if (map.value !=null){

            val userMarker = MarkerOptions()
            userMarker.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_person))
            userMarker.position(userLatLng!!)
            map.value?.addMarker(userMarker)
            map.value?.animateCamera((CameraUpdateFactory.newLatLngZoom(userLatLng!!,13f)))

            val resMarker =MarkerOptions()
            resMarker.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_restaurant))
            resMarker.position(LatLng(restaurant?.address?.latitude!!, restaurant?.address?.longitude!!))
            map.value?.addMarker(resMarker)
        }

    }
    private fun getRestaurant(){
        viewModelScope.launch {
          repository.getRestaurantById(restaurantId!!, object : RetrofitInterface {
              override fun onSuccess(result: Any) {
                  restaurant = result as RestaurantModel
                  if (userLatLng !=null){

                      initMap()
                  }
              }
              override fun onError(e: Exception) {
                  Toast.makeText(activity, e.message, Toast.LENGTH_SHORT).show()
              }
          })
        }
    }





}