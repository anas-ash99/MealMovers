package com.example.mealmoverskotlin.ui.address

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.mealmoverskotlin.R
import com.example.mealmoverskotlin.data.models.AddressModel
import com.example.mealmoverskotlin.data.models.googleModls.GoogleLatlngResponse
import com.example.mealmoverskotlin.databinding.ActivityAddAddressMapBinding
import com.example.mealmoverskotlin.shared.LastSeenLocation
import com.example.mealmoverskotlin.domain.geoapify.Geoapify
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
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@SuppressLint("MissingPermission")
@AndroidEntryPoint
class AddAddressMapActivity : AppCompatActivity() {


    private lateinit var binding:ActivityAddAddressMapBinding
    private lateinit var mapView:SupportMapFragment
    private lateinit var map:GoogleMap
    private lateinit var geoapify: Geoapify
    private lateinit var locationManager: LocationManager

    @Inject
    lateinit var googleGeocoding: GoogleGeocoding
    private val centerLatLag = LatLng(50.985653,10.322245)
    private val address: MutableLiveData<AddressModel?> by lazy {
        MutableLiveData<AddressModel?>()
    }

    val marker = MarkerOptions()
    private var isSatellite = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= DataBindingUtil.setContentView(this, R.layout.activity_add_address_map)
        geoapify = Geoapify(this)
        address.value = null
        initFragment()
        changeMapType()
        onMyLocationClick()
        observeAddress()
        onDoneClick()
        onArrowBackClick()


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


    private fun initFragment() {
        mapView = supportFragmentManager.findFragmentById(R.id.mapView2) as SupportMapFragment
        mapView.getMapAsync {
            map = it
            map.uiSettings.isMyLocationButtonEnabled = false
            onMapClick()
            if (LastSeenLocation.isLocationPermissionGranted(this)){
                enableMyLocationOnMap()
            }else{
                LastSeenLocation.askForLocationPermission(this)
            }

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

        val latlng = "${latLng.latitude}, ${latLng.longitude}"
        googleGeocoding.getAddressByLatlng(latlng, object : OnDone {
            override fun onLoadingDone(res1: Any?) {

                try {
                    val res:GoogleLatlngResponse = res1 as GoogleLatlngResponse
                    address.value = AddressModel(streetName = res.results[0].address_components[1].long_name,
                        zipCode = res.results[0].address_components[7].long_name,
                        city = res.results[0].address_components[2].long_name,
                        houseNumber = res.results[0].address_components[0].long_name
                    )
                    marker.title("${address.value?.streetName} ${address.value?.houseNumber}")
                    map.addMarker(marker)
                }catch (e:Exception){
                    Toast.makeText(this@AddAddressMapActivity, "Can't use this address", Toast.LENGTH_SHORT).show()
                }


            }

            override fun onError(e: Exception) {
                Toast.makeText(this@AddAddressMapActivity, e.message.toString(), Toast.LENGTH_SHORT).show()
            }
        })


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


    private fun enableMyLocationOnMap(){
        map.isMyLocationEnabled = true
        map.animateCamera((CameraUpdateFactory.newLatLngZoom(DataHolder.myLatLng.value!!,17f)))

    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1){
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                LastSeenLocation.setLastSeenLocation(this@AddAddressMapActivity)
                map.isMyLocationEnabled = true
                binding.myLocation.visibility = View.VISIBLE
            } else {
                Toast.makeText(this, "Permission was rejected", Toast.LENGTH_SHORT).show()
                binding.myLocation .visibility = View.GONE
                map.animateCamera((CameraUpdateFactory.newLatLngZoom(centerLatLag,5f)))
            }

        }
    }
}