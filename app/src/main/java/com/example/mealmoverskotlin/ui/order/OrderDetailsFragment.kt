package com.example.mealmoverskotlin.ui.order

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mealmoverskotlin.R
import com.example.mealmoverskotlin.databinding.FragmentOrderDetailsBinding
import com.example.mealmoverskotlin.domain.adapters.AdapterOrderItem
import com.example.mealmoverskotlin.domain.viewModels.OrderPageViewModel

class OrderDetailsFragment : Fragment() {


    private lateinit var binding:FragmentOrderDetailsBinding
    private lateinit var viewModel: OrderPageViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding= DataBindingUtil.inflate(inflater,R.layout.fragment_order_details, container, false)
        viewModel = ViewModelProvider(requireActivity())[OrderPageViewModel::class.java]
        binding.order = viewModel.order
        binding.restaurant = viewModel.restaurant
        initItemsRecyclerView()
        onArrowBackClick()
        return binding.root
    }
    private fun onArrowBackClick() { binding.backArrow.setOnClickListener { requireActivity().onBackPressed() } }
    private fun initItemsRecyclerView() {
        val adapter = AdapterOrderItem(requireContext(), viewModel.order?.items!!)
        binding.recyclerview.adapter = adapter
        binding.recyclerview.layoutManager = LinearLayoutManager(requireContext())
    }


}