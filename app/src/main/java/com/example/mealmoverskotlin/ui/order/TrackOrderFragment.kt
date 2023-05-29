package com.example.mealmoverskotlin.ui.order

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.mealmoverskotlin.R
import com.example.mealmoverskotlin.databinding.FragmentTrackOrderBinding
import com.example.mealmoverskotlin.ui.adapters.AdapterOrderItem
import com.example.mealmoverskotlin.domain.viewModels.OrderPageViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


class TrackOrderFragment : Fragment() {


    private lateinit var binding:FragmentTrackOrderBinding
    private lateinit var viewModel: OrderPageViewModel
    private lateinit var map:GoogleMap
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_track_order, container, false)
        viewModel = ViewModelProvider(requireActivity())[OrderPageViewModel::class.java]
        getMap()
        initPageValues()
        onSeerOrderDetailsClick()

        return binding.root
    }


    private fun onSeerOrderDetailsClick() {
       binding.orderInfoButton.setOnClickListener {
              startFragment()
       }
    }

    private fun getMap() {

        try {
            val mapFragment: SupportMapFragment = childFragmentManager.findFragmentById(R.id.mapView) as SupportMapFragment
            mapFragment.getMapAsync {
                map = it
                initMap()
            }
        }catch (e:Exception){
            Log.e("Map", e.message!!, e)
            Toast.makeText(activity, "Something went wrong initializing the map", Toast.LENGTH_SHORT).show()
        }
    }


    private fun initMap(){
        if (map !=null){

            val userMarker = MarkerOptions()
            userMarker.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_person))
            userMarker.position(viewModel.userLatLng!!)
            map.addMarker(userMarker)
            map.animateCamera((CameraUpdateFactory.newLatLngZoom(viewModel.userLatLng!!,13f)))
            val resMarker =MarkerOptions()
            resMarker.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_restaurant))
            resMarker.position(LatLng(viewModel.restaurant?.address?.latitude!!, viewModel.restaurant?.address?.longitude!!))
            map.addMarker(resMarker)
        }

    }

    @SuppressLint("SetTextI18n")
    private fun initPageValues(){
        when (viewModel.order?.status) {
            "confirmed" -> binding.orderConfirmedCheckMark.visibility = View.VISIBLE
            "kitchen" -> {
                binding.orderConfirmedCheckMark.visibility = View.VISIBLE
                binding.orderKitchenCheckMark.visibility = View.VISIBLE
            }
            "being delivered" -> {
                binding.orderConfirmedCheckMark.visibility = View.VISIBLE
                binding.orderKitchenCheckMark.visibility = View.VISIBLE
                binding.orderBeingDeliveredCheckMark.visibility = View.VISIBLE
                binding.deliveryPerson.visibility = View.VISIBLE
                binding.orderStatus.text = "Order on the way"
            }
            "delivered" -> {

                startFragmentNoBackStack()

            }
        }

    }

    private fun startFragmentNoBackStack() {
        requireActivity().supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_layout,OrderDetailsFragment(), "order_details")
            commit()
        }
    }

    private fun startFragment(){
        requireActivity().supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_layout,OrderDetailsFragment(), "order_details")
            addToBackStack("")
            commit()
        }

    }
}