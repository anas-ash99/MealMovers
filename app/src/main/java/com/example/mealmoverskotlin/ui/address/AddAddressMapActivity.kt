package com.example.mealmoverskotlin.ui.address

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.example.mealmoverskotlin.R
import com.example.mealmoverskotlin.data.dataStates.DataState
import com.example.mealmoverskotlin.data.models.AddressModel
import com.example.mealmoverskotlin.data.models.geoapifyModels.ResultsModel
import com.example.mealmoverskotlin.data.models.googleModls.GoogleLatlngResponse
import com.example.mealmoverskotlin.databinding.ActivityAddAddressMapBinding
import com.example.mealmoverskotlin.domain.geoapify.Geoapify
import com.example.mealmoverskotlin.domain.geoapify.GetAddressByLatlng
import com.example.mealmoverskotlin.domain.google.GoogleGeocoding
import com.example.mealmoverskotlin.domain.google.OnDone
import com.example.mealmoverskotlin.shared.Constants
import com.example.mealmoverskotlin.shared.DataHolder
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.launch


class AddAddressMapActivity : AppCompatActivity(), LocationListener {


    private lateinit var binding:ActivityAddAddressMapBinding
    private lateinit var mapView:SupportMapFragment
    private lateinit var map:GoogleMap
    private lateinit var geoapify: Geoapify
    private lateinit var locationManager: LocationManager
    private lateinit var googleGeocoding: GoogleGeocoding
    private val address: MutableLiveData<AddressModel?> by lazy {
        MutableLiveData<AddressModel?>()
    }
    private val addressResponse:MutableLiveData<DataState<ResultsModel>> by lazy {
        MutableLiveData<DataState<ResultsModel>>()
    }
    val marker = MarkerOptions()
    private var isSatellite = false

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= DataBindingUtil.setContentView(this, R.layout.activity_add_address_map)
        geoapify = Geoapify(this)
        googleGeocoding = GoogleGeocoding(this )
        address.value = null
        initFragment()
        changeMapType()
        onMyLocationClick()
        observeAddress()
        onDoneClick()
        onArrowBackClick()
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,5000, 1f, this)

    }

    private fun onDoneClick() {
        binding.DoneButton.setOnClickListener{
            if (DataHolder.userAddress != null){
                address.value?.name = DataHolder.userAddress?.name!!
                DataHolder.userAddress = address.value

            }else{
                DataHolder.userAddress = address.value
                DataHolder.userAddress?.name = DataHolder.loggedInUser?.fullName!!

            }
            val intent =Intent()
            intent.putExtra("ADDRESS", address.value)
            setResult(Constants.ADDRESS_RESULT_CODE, intent)
            onBackPressed()




        }
    }

    private fun observeAddress() {
        address.observe(this, Observer {
            if (it == null){
                binding.DoneButton.visibility = View.GONE

            }else{
                binding.DoneButton.visibility = View.VISIBLE
            }
        })
    }

    @SuppressLint("MissingPermission")
    private fun initFragment() {
        mapView = supportFragmentManager.findFragmentById(R.id.mapView2) as SupportMapFragment
        mapView.getMapAsync {
            map = it
            onMapClick()
            it.isMyLocationEnabled = true

            it.animateCamera((CameraUpdateFactory.newLatLngZoom(DataHolder.myLatLng!!,20f)))
            println(DataHolder.myLatLng!!)
        }
    }

    private fun onMapClick() {
        map.setOnMapClickListener {
            address.value = null
            map.clear()
            marker.position(it)
            marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN))
//            marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.gps_location_map_marker_navigation_icon))

            map.animateCamera((CameraUpdateFactory.newLatLngZoom(it,20f)))
            getAddress(it)


        }
    }

    private fun onArrowBackClick(){
        binding.backArrow.setOnClickListener {
            onBackPressed()
        }
    }

    private fun getAddress(latLng: LatLng) {

        val latlang = "${latLng.latitude}, ${latLng.longitude}"
        googleGeocoding.getAddressByLatlng(latlang, object : OnDone {
            override fun onLoadingDone(res1: Any?) {
                val res:GoogleLatlngResponse = res1 as GoogleLatlngResponse
                println(res.results[0].formatted_address)
                address.value = AddressModel(streetName = res.results[0].address_components[1].long_name,
                    zipCode = res.results[0].address_components[7].long_name,
                    city = res.results[0].address_components[2].long_name,
                    houseNumber = res.results[0].address_components[0].long_name
                )
                marker.title("${address.value?.streetName} ${address.value?.houseNumber}")
                map.addMarker(marker)

            }

            override fun onError(e: Exception) {
                Toast.makeText(this@AddAddressMapActivity, e.message.toString(), Toast.LENGTH_SHORT).show()
            }
        })

//        lifecycleScope.launch {
//            geoapify.getAddressByLatlng(latLng.latitude, latLng.longitude,object : GetAddressByLatlng {
//                override fun onLoadingDone(res: ResultsModel) {
////                    address.value = AddressModel(streetName = res.results[0].street!!,
////                        zipCode = res.results[0].postcode!!,
////                        city = res.results[0].city!!,
////                        houseNumber = res.results[0].housenumber!!
////                    )
//
//                }
//                override fun onFailure(e: Exception) {
//                    Toast.makeText(this@AddAddressMapActivity, e.message.toString(), Toast.LENGTH_SHORT).show()
//                }
//            })
//        }
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
            map.animateCamera((CameraUpdateFactory.newLatLngZoom(DataHolder.myLatLng!!,17f)))
        }


    }

    override fun onLocationChanged(location: Location) {
//        map.addMarker(MarkerOptions().position(LatLng(location.latitude, location.longitude)))
//        println(location.longitude)
//        println(location.latitude)
    }
}