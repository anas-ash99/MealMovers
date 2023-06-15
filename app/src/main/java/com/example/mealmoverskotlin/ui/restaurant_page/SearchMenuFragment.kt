package com.example.mealmoverskotlin.ui.restaurant_page

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mealmoverskotlin.data.models.MenuItemModel
import com.example.mealmoverskotlin.databinding.FragmentSearchMenuBinding
import com.example.mealmoverskotlin.ui.adapters.AdapterMenuItems
import com.example.mealmoverskotlin.domain.viewModels.RestaurantAndCartVM
import com.example.mealmoverskotlin.ui.dialogs.MenuItemDialog


class SearchMenuFragment : Fragment() {


    private lateinit var binding:FragmentSearchMenuBinding
    private lateinit var viewModel:RestaurantAndCartVM
    private lateinit var dialog: MenuItemDialog
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentSearchMenuBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(requireActivity())[RestaurantAndCartVM::class.java]
        binding.editText.requestFocus()
        dialog = MenuItemDialog(requireContext(), viewModel.cartEvent)
        onArrowBack()
        handelUserInput()
        observeSearchItems()
        initRecyclerView(viewModel.restaurant.menu_items)
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
                initRecyclerView(viewModel.restaurant.menu_items)
                binding.xButton.visibility = View.GONE
            }

        }

        binding.xButton.setOnClickListener {
            binding.editText.setText("")
        }
    }

    private fun initRecyclerView(list: List<MenuItemModel>) {
        try {

            val adapter = AdapterMenuItems(requireActivity(), list, ::onItemClick)
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

    private fun onItemClick(item:MenuItemModel){
        dialog.showDialog(item)
    }

}