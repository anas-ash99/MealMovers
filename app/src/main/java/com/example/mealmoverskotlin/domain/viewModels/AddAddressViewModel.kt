package com.example.mealmoverskotlin.domain.viewModels

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mealmoverskotlin.data.models.AddressModel
import com.example.mealmoverskotlin.data.models.googleModls.GoogleLatlngResponse
import com.example.mealmoverskotlin.databinding.ActivityAddressBinding
import com.example.mealmoverskotlin.shared.LastSeenLocation
import com.example.mealmoverskotlin.ui.adapters.AddressSearchAdapter
import com.example.mealmoverskotlin.domain.geoapify.Geoapify
import com.example.mealmoverskotlin.domain.google.GoogleAddressAutoComplete
import com.example.mealmoverskotlin.domain.google.GoogleGeocoding
import com.example.mealmoverskotlin.domain.google.OnDone
import com.example.mealmoverskotlin.domain.repositorylnterfaces.RestaurantRepositoryInterface
import com.example.mealmoverskotlin.domain.repositorylnterfaces.SharedPreferencesRepository
import com.example.mealmoverskotlin.shared.DataHolder
import com.example.mealmoverskotlin.shared.KeyboardManger.hideSoftKeyboard
import com.example.mealmoverskotlin.shared.KeyboardManger.showSoftKeyboard
import com.example.mealmoverskotlin.ui.address.AddAddressMapActivity
import com.example.mealmoverskotlin.ui.address.AddressActivity
import com.example.mealmoverskotlin.ui.mainPage.MainActivity
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.model.RectangularBounds
import com.google.android.libraries.places.api.model.TypeFilter
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse
import com.google.android.libraries.places.api.net.PlacesClient
import com.stripe.exception.ApiException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AddAddressViewModel @Inject constructor(
    private val sharedPreferencesRepository: SharedPreferencesRepository,
    private val addressAutoComplete: GoogleAddressAutoComplete,
    private val googleGeocoding: GoogleGeocoding,
) : ViewModel() {


    var isAfterSignUp:Boolean? = false
    private var adapter: AddressSearchAdapter? = null
    val currentAddress by lazy {
        MutableLiveData<AddressModel?>(null)
    }
    val isLocationPermissionGranted by lazy {
        MutableLiveData<Boolean>()
    }
    val isAddAddressLayoutShown by lazy {
        MutableLiveData<Boolean>()
    }
    val suggestedAddressesList by lazy {
        MutableLiveData<List<AutocompletePrediction>>()
    }
    var address:AddressModel? = null
    val suggestedAddress by lazy {
        MutableLiveData<AddressModel>()
    }


    fun searchForAddress(input:String){
        addressAutoComplete.googleAutoComplete(input){

            suggestedAddressesList.value = it
        }
    }


    fun setSuggestedAddress(){
        DataHolder.myLatLng.value?.let { it ->

            getAddressByLatLang(it){address->
                suggestedAddress.value = address
            }
        }
    }


    fun initAddress(newAddress:AddressModel){

        currentAddress.value = AddressModel()
        currentAddress.value?.city = newAddress.city
        currentAddress.value?.zipCode= newAddress.zipCode
        currentAddress.value?.houseNumber = newAddress.houseNumber
        currentAddress.value?.streetName = newAddress.streetName
    }

    fun getAddressByLatLang(latLng:LatLng ,onResult: (AddressModel?) ->Unit){
        viewModelScope.launch {
            DataHolder.myLatLng.value?.let {
                val latLngString = "${latLng.latitude}, ${latLng.longitude}"

                googleGeocoding.getAddressByLatlng(latLngString, object : OnDone {
                    override fun onLoadingDone(res1: Any?) {
                        val res:GoogleLatlngResponse = res1 as GoogleLatlngResponse

                        try {
                            val  address = AddressModel()
                            address.apply {
                                city = res.results[0].address_components[2].long_name
                                houseNumber = res.results[0].address_components[0].long_name
                                streetName = res.results[0].address_components[1].long_name
                                zipCode = res.results[0].address_components[7].long_name
                            }
                            onResult(address)

                        }catch (e:Exception){

                            suggestedAddress.value = null
                            onResult(null)
                            Log.e("Fetch address", e.message, e)
                        }



                    }

                    override fun onError(e: Exception) {
                        Log.e("Fetch Address", e.message, e)
                    }
                })
            }
            }

    }

    fun updateAddress(){
//        DataHolder.userAddress?.city = currentAddress?.city!!
//        DataHolder.userAddress?.zipCode= address?.zipCode!!
//        DataHolder.userAddress?.houseNumber = address?.houseNumber!!
//        DataHolder.userAddress?.streetName = address?.streetName!!
        DataHolder.userAddress = currentAddress.value
        DataHolder.userAddress?.name = DataHolder.loggedInUser?.fullName!!
        viewModelScope.launch {

            sharedPreferencesRepository.updateUserAddress(DataHolder.userAddress!!)
        }
    }

//
//    fun init(activity: AddressActivity, binding: ActivityAddressBinding){
//        this.binding = binding
//        this.activity = activity
//        geoapify= Geoapify(activity)
//        isAfterSignUp =  activity.intent.getBooleanExtra("isAfterSignUp", false)
//        handleSearch()
//        onAddAddressClick()
//        onDoneButtonClick()
//        initGoogleAutoComplete()
//        onCantFindAddressClick()
//        onArrowBackClick()
//        onCurrentLocationClick()
//        LastSeenLocation.askForLocationPermission(activity)
//        setCurrentLocation()
//        if (DataHolder.userAddress != null){
//            initAddress(DataHolder.userAddress!!)
//        }
//        binding.address = address
//    }
//
//
//

//    private fun onCantFindAddressClick(){
//        try {
//            binding.cantFindAddress.setOnClickListener {
//               activity.startActivity(Intent(activity, AddAddressMapActivity::class.java))
//               binding.addAddressLayout.visibility = View.GONE
//               binding.editTextSearch.setText("")
//            }
//        }catch (e:Exception){
//            Log.e("Order", e.message!!,e)
//        }
//
//
//    }
//    fun onAddressItemClick(address:AddressModel){
//
//
//
//
//
//        if (address.city == "" || address.houseNumber == "" || address.streetName == "" || address.zipCode == ""){
//            binding.editTextSearch.setText("${address.address_line1}, ${address.address_line2}")
//            binding.editTextSearch.setSelection(binding.editTextSearch.text.length)
//
//        }else{
//            initAddress(address)
//            binding.address = this.address
//            hideSearchBar()
//
//        }
//
//
//
//
//    }
//
//
//     private fun handleSearch(){
//         binding.editTextSearch.addTextChangedListener {
//             if (it?.toString()?.trim() != ""){
//
//                 binding.currentAddressLayout.visibility =View.GONE
//                googleAutoComplete(it?.toString()?.trim()!!)
////                 initRecyclerView(it?.toString()?.trim()!!)
//             }else{
//                 initRecyclerViewGoogle(mutableListOf())
//                 binding.currentAddressLayout.visibility =View.VISIBLE
//             }
//
//
//         }
//
//
//
//     }
//    private fun onAddAddressClick() {
//        binding.addressBar.setOnClickListener {
//            showSearchBar()
//        }
//        binding.searchBackArrow.setOnClickListener{
//            hideSearchBar()
//        }
//
//
//    }
//
//    private fun onCurrentLocationClick(){
//        binding.currentAddressLayout.setOnClickListener {
//
//            if (suggestedAddress == null){
//                LastSeenLocation.setLastSeenLocation(activity)
//            }else{
//                if (address == null){
//                    address = suggestedAddress
//                    DataHolder.userAddress = address
//                }else{
//
//                    initAddress(suggestedAddress!!)
//                }
//                binding.address = suggestedAddress
//                hideSearchBar()
//            }
//
//        }
//    }
//
//





//
//    private fun onDoneButtonClick(){
//
//        binding.doneButton.setOnClickListener {
//            if (address == null){
//                Toast.makeText(activity, "Please enter your address", Toast.LENGTH_SHORT).show()
//            }
//            else{
//                if (isAfterSignUp == false || isAfterSignUp == null){
//                    activity.onBackPressed()
//
//                }else{
//                    activity.startActivity(Intent(activity, MainActivity::class.java))
//                }
//                println(address)
//                address?.let { updateAddress() }
//
//            }
//        }
//
//    }
//

//
//    private fun onArrowBackClick(){
//
//        binding.arrowBack.setOnClickListener {
//            activity.onBackPressed()
//        }
//
//    }
//
//
//
//    private fun initGoogleAutoComplete(){
//       token = AutocompleteSessionToken.newInstance()
//        val bounds = RectangularBounds.newInstance(
//            LatLng(-33.880490, 151.184363),
//            LatLng(-33.858754, 151.229596)
//        )
//        Places.initialize(activity, "AIzaSyABajkttb898xIQBfmwcfXjw89SIRbP83o")
//        placesClient = Places.createClient(activity)
//    }
//    private fun googleAutoComplete(text:String){
//
//
//        val request = FindAutocompletePredictionsRequest.builder()
//                .setCountry("DE")
//                .setTypeFilter(TypeFilter.ADDRESS)
//                .setSessionToken(token)
//                .setQuery(text)
//                .build()
//
//        placesClient.findAutocompletePredictions(request)
//            .addOnSuccessListener { response: FindAutocompletePredictionsResponse ->
//                initRecyclerViewGoogle(response.autocompletePredictions)
//
//            }.addOnFailureListener { exception: Exception? ->
//                if (exception is ApiException) {
//                    Log.e("TAG", "Place not found: ${exception.statusCode}")
//                }
//            }
//    }



}