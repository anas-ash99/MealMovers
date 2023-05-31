package com.example.mealmoverskotlin.ui.address

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.mealmoverskotlin.R
import com.example.mealmoverskotlin.data.models.AddressModel
import com.example.mealmoverskotlin.databinding.ActivityAddAddressMapBinding
import com.example.mealmoverskotlin.databinding.FragmentSelectAddressFromMapBinding
import com.example.mealmoverskotlin.domain.geoapify.Geoapify
import com.example.mealmoverskotlin.domain.viewModels.AddAddressViewModel
import com.example.mealmoverskotlin.shared.DataHolder
import com.example.mealmoverskotlin.shared.LastSeenLocation
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.MarkerOptions


class SelectAddressFromMapFragment : Fragment() {

    private lateinit var binding: FragmentSelectAddressFromMapBinding
    private lateinit var viewModel:AddAddressViewModel
    private lateinit var mapView: SupportMapFragment
    private lateinit var map: GoogleMap
    private lateinit var geoapify: Geoapify
    private val address by lazy {
        MutableLiveData<AddressModel?>()
    }
    private val marker by lazy { MarkerOptions() }
    private var isSatellite = false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSelectAddressFromMapBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(requireActivity())[AddAddressViewModel::class.java]
        initFragment()
        changeMapType()
        onMyLocationClick()
        observeAddress()
        onDoneClick()
        onArrowBackClick()

        return binding.root
    }



    private fun onDoneClick() {
        binding.DoneButton.setOnClickListener{

            viewModel.initAddress(address.value!!)
            requireActivity().onBackPressed()

        }
    }

    private fun observeAddress() {
        address.observe(requireActivity()) {

            if (it == null) {
                binding.DoneButton.visibility = View.GONE
            } else {
                binding.DoneButton.visibility = View.VISIBLE
            }
        }
    }


    private fun initFragment() {
        mapView = childFragmentManager.findFragmentById(R.id.mapView2) as SupportMapFragment
        mapView.getMapAsync {
            map = it
            map.uiSettings.isMyLocationButtonEnabled = false
            onMapClick()
            if (LastSeenLocation.isLocationPermissionGranted(requireActivity())){
                enableMyLocationOnMap()
            }else{
                LastSeenLocation.askForLocationPermission(requireActivity())
            }

        }
    }


    private fun addressCallBack(addressModel: AddressModel?){

            if (addressModel == null){
                Toast.makeText(requireContext(), "Can't use this address", Toast.LENGTH_SHORT).show()
            }else{
                address.value = addressModel
                marker.title("${address.value?.streetName} ${address.value?.houseNumber}")
                map.addMarker(marker)
            }

    }
    private fun onMapClick() {
        map.setOnMapClickListener { it ->
            address.value = null
            map.clear()
            marker.position(it)
            marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN))
            viewModel.getAddressByLatLang(it, ::addressCallBack)
//            marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.gps_location_map_marker_navigation_icon))
            map.animateCamera((CameraUpdateFactory.newLatLngZoom(it,20f)))

        }
    }

    private fun onArrowBackClick(){
        binding.backArrow.setOnClickListener {
            requireActivity().onBackPressed()

        }
    }




    private fun changeMapType(){
        binding.mapType.setOnClickListener {
            if (isSatellite){
                map.mapType = GoogleMap.MAP_TYPE_NORMAL
                isSatellite = false
            }else{
                map.mapType = GoogleMap.MAP_TYPE_SATELLITE
                isSatellite = true
            }
        }


    }

    private fun onMyLocationClick(){
        binding.myLocation.setOnClickListener {
            map.animateCamera((CameraUpdateFactory.newLatLngZoom(DataHolder.myLatLng.value!!,17f)))
        }


    }


    @SuppressLint("MissingPermission")
    private fun enableMyLocationOnMap(){
        map.isMyLocationEnabled = true
        map.animateCamera((CameraUpdateFactory.newLatLngZoom(DataHolder.myLatLng.value!!,17f)))

    }


}