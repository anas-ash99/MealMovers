package com.example.mealmoverskotlin.domain.google

import android.content.Context
import android.util.Log
import com.example.mealmoverskotlin.BuildConfig
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

class GoogleAddressAutoComplete (
    private val context: Context
        ) {
    private lateinit var token: AutocompleteSessionToken
    private lateinit var placesClient: PlacesClient
   init {
       initGoogleAutoComplete()

   }
    private fun initGoogleAutoComplete(){
        token = AutocompleteSessionToken.newInstance()
        val bounds = RectangularBounds.newInstance(
            LatLng(-33.880490, 151.184363),
            LatLng(-33.858754, 151.229596)
        )
        Places.initialize(context, BuildConfig.MAPS_API_KEY)
        placesClient = Places.createClient(context)
    }

    fun googleAutoComplete(text:String, onResult:(List<AutocompletePrediction>) ->Unit){

        val request = FindAutocompletePredictionsRequest.builder()
            .setCountry("DE")
            .setTypeFilter(TypeFilter.ADDRESS)
            .setSessionToken(token)
            .setQuery(text)
            .build()

        placesClient.findAutocompletePredictions(request)
            .addOnSuccessListener { response: FindAutocompletePredictionsResponse ->
//                initRecyclerViewGoogle(response.autocompletePredictions)
                onResult(response.autocompletePredictions)
            }.addOnFailureListener { exception: Exception? ->
                if (exception is ApiException) {
                    Log.e("TAG", "Place not found: ${exception.statusCode}")
                }
            }
    }
}