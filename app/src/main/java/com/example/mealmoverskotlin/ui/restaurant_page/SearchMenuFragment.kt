package com.example.mealmoverskotlin.ui.restaurant_page

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mealmoverskotlin.data.models.MenuItemModel
import com.example.mealmoverskotlin.databinding.FragmentSearchMenuBinding
import com.example.mealmoverskotlin.ui.adapters.AdapterMenuItems
import com.example.mealmoverskotlin.domain.viewModels.RestaurantAndCheckoutVM
import com.example.mealmoverskotlin.shared.KeyboardManger.showSoftKeyboard


class SearchMenuFragment : Fragment() {


    private lateinit var binding:FragmentSearchMenuBinding
    private lateinit var viewModel:RestaurantAndCheckoutVM
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentSearchMenuBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(requireActivity())[RestaurantAndCheckoutVM::class.java]
        binding.editText.requestFocus()
        onArrowBack()
        handelUserInput()
        observeSearchItems()

        return binding.root
    }



    private fun observeSearchItems() {
        viewModel.itemsSearchFor.observe(requireActivity()){
            initRecyclerView(it)
        }
    }


    private fun handelUserInput() {
        binding.editText.addTextChangedListener {
            if (it?.toString()?.trim() != ""){
                binding.xButton.visibility = View.VISIBLE
                viewModel.handleSearchItems(it?.toString()?.trim()!!)
            }else{
              initRecyclerView(listOf())
                binding.xButton.visibility = View.GONE
            }

        }

        binding.xButton.setOnClickListener {
            binding.editText.setText("")
        }
    }

    private fun initRecyclerView(list: List<MenuItemModel>) {
        try {

            val adapter = AdapterMenuItems(requireActivity(), list, viewModel)
            binding.recyclerview.adapter = adapter
            binding.recyclerview.layoutManager = LinearLayoutManager(requireContext())
        }catch (e:Exception){
           Log.e("search fragment",e.message, e)
        }
    }

    private fun onArrowBack() {
        binding.arrowBack.setOnClickListener {

            requireActivity().onBackPressed()
        }
    }

}