package com.example.mealmoverskotlin.ui.address

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mealmoverskotlin.R
import com.example.mealmoverskotlin.data.models.AddressModel
import com.example.mealmoverskotlin.databinding.FragmentAddressHomePageBinding
import com.example.mealmoverskotlin.databinding.FragmentSelectAddressFromMapBinding
import com.example.mealmoverskotlin.domain.viewModels.AddAddressViewModel
import com.example.mealmoverskotlin.shared.DataHolder
import com.example.mealmoverskotlin.shared.KeyboardManger.hideSoftKeyboard
import com.example.mealmoverskotlin.shared.KeyboardManger.showSoftKeyboard
import com.example.mealmoverskotlin.shared.LastSeenLocation
import com.example.mealmoverskotlin.ui.adapters.AddressSearchAdapter
import com.google.android.libraries.places.api.model.AutocompletePrediction
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddressHomePageFragment : Fragment() {


    private lateinit var binding:FragmentAddressHomePageBinding
    private lateinit var viewModel:AddAddressViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity())[AddAddressViewModel::class.java]
        DataHolder.userAddress?.let {viewModel.initAddress(it)  }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentAddressHomePageBinding.inflate(layoutInflater)
        binding.addressSearchBar.animate().translationY(-163.0f).duration = 0
        checkLocationPermission()
        onAddAddressClick()
        onArrowBackClick()
        observeIsAddressShown()
        handleSearch()
        observeSuggestedAddressesList()
        observeSuggestedAddress()
        observeMyLatLang()
        observeCurrentAddress()
        onSuggestedAddressCardClick()
        onDoneButtonClick()
        onCantFindMyAddressClick()


        return binding.root
    }

    private fun checkLocationPermission() {
        if (LastSeenLocation.isLocationPermissionGranted(requireActivity())){
            LastSeenLocation.setLastSeenLocation(requireActivity())

        }else{
            LastSeenLocation.askForLocationPermission(requireActivity())
        }
    }

    private fun onDoneButtonClick() {
        binding.doneButton.setOnClickListener {
            if (viewModel.currentAddress.value == null){
                Toast.makeText(requireContext(), "Please Enter your address", Toast.LENGTH_SHORT).show()
            }else{
                viewModel.updateAddress()
                requireActivity().onBackPressed()
            }

        }
    }




    private fun observeCurrentAddress() {
        viewModel.currentAddress.observe(requireActivity()){
            it?.let { binding.address = it }
        }
    }

    private fun observeMyLatLang() {
        DataHolder.myLatLng.observe(requireActivity()){
            it?.let { viewModel.setSuggestedAddress()}
        }
    }

    private fun observeSuggestedAddress() {
        viewModel.suggestedAddress.observe(requireActivity()){
            binding.suggestedAddress = it

        }
    }


    private fun observeIsAddressShown() {
        viewModel.isAddAddressLayoutShown.observe(requireActivity()){
            if (it){
                showSearchBar()
            }else{
                hideSearchBar()
            }
        }
    }


    private fun showSearchBar(){
        binding.addressSearchBar.animate().translationY(0f).duration = 200
        binding.addAddressLayout.visibility = View.VISIBLE
        binding.editTextSearch.requestFocus()
        showSoftKeyboard(binding.editTextSearch,requireContext())

    }
    private fun onAddAddressClick() {
        binding.addressBar.setOnClickListener {
//            showSearchBar()
            viewModel.isAddAddressLayoutShown.value = true
        }

        binding.searchBackArrow.setOnClickListener{
            viewModel.isAddAddressLayoutShown.value = false
        }

    }

    private fun onArrowBackClick() {
        binding.arrowBack.setOnClickListener{
            requireActivity().onBackPressed()
        }
    }



    //////////////////////    addAddressLayout related functions


    private fun hideSearchBar(){
        binding.addressSearchBar.animate().translationY(-binding.addressSearchBar.height.toFloat()).duration = 200
        lifecycleScope.launch {
            delay(200)
            binding.addAddressLayout.visibility = View.GONE
            initAddressRecyclerView(listOf())
            hideSoftKeyboard(binding.editTextSearch,requireContext())
            binding.editTextSearch.clearFocus()
            binding.editTextSearch.setText("")


        }

    }

    private fun handleSearch() {
        binding.editTextSearch.addTextChangedListener {
            if (it?.toString()?.trim() != "") {

                binding.suggestedAddressCard.visibility = View.GONE
                viewModel.searchForAddress(it?.toString()?.trim()!!)

            } else {
//                initRecyclerViewGoogle(mutableListOf())
                binding.suggestedAddressCard.visibility = View.VISIBLE
            }


        }

     }

    private fun observeSuggestedAddressesList() {
        viewModel.suggestedAddressesList.observe(requireActivity()){
             initAddressRecyclerView(it)
        }
    }

    private fun initAddressRecyclerView(list:List<AutocompletePrediction>){

           val  adapter = AddressSearchAdapter(requireActivity(), list, ::onAddressItemClick )
            binding.recyclerview.adapter =adapter
            binding.recyclerview.layoutManager = LinearLayoutManager(requireContext())

    }

    @SuppressLint("SetTextI18n")
    private fun onAddressItemClick(address:AddressModel){

        if (address.city == "" || address.houseNumber == "" || address.streetName == "" || address.zipCode == ""){
            binding.editTextSearch.setText("${address.address_line1}, ${address.address_line2}")
            binding.editTextSearch.setSelection(binding.editTextSearch.text.length)
        }else{
            viewModel.initAddress(address)
            viewModel.isAddAddressLayoutShown.value = false
        }
    }

    private fun onSuggestedAddressCardClick() {
        binding.suggestedAddressCard.setOnClickListener {

            viewModel.suggestedAddress.value?.let {
                viewModel.initAddress(it)
                viewModel.isAddAddressLayoutShown.value = false
            }
        }
    }

    private fun onCantFindMyAddressClick(){
        binding.cantFindAddress.setOnClickListener {
            viewModel.isAddAddressLayoutShown.value = false
            requireActivity().supportFragmentManager.commit {
                replace(R.id.fragment_layout, SelectAddressFromMapFragment(), "map_fragment")
                addToBackStack(null)
            }
        }
    }


}
