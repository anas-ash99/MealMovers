package com.example.mealmoverskotlin.ui.restaurant_page

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.example.mealmoverskotlin.R
import com.example.mealmoverskotlin.data.events.CartEvent
import com.example.mealmoverskotlin.data.models.RestaurantModel
import com.example.mealmoverskotlin.databinding.ActivityRestaurantBinding
import com.example.mealmoverskotlin.domain.viewModels.RestaurantAndCartVM
import com.example.mealmoverskotlin.ui.dialogs.RestaurantClosedDialog
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class RestaurantActivity : AppCompatActivity() {
    private lateinit var binding:ActivityRestaurantBinding
    val viewModel:RestaurantAndCartVM by viewModels()
    private lateinit var closedDialog: RestaurantClosedDialog
//    private lateinit var menuItemDialog:MenuItemDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_restaurant)
        viewModel.restaurant =  intent.getSerializableExtra("RESTAURANT") as RestaurantModel
        viewModel.init()
        closedDialog = RestaurantClosedDialog(this)
        startFragment()
        observeCartEvent()
        observeIsRestaurantOpen()


    }

    private fun observeIsRestaurantOpen() {
        viewModel.isResOpen.observe(this){
            if (!it){
                closedDialog.showDialog()
            }
        }
    }

    private fun observeCartEvent() {
        viewModel.cartEvent.observe(this){

            when(it){
                is CartEvent.AddItem -> {
                    viewModel.addItemToCart(it.item, it.qut) }
                is CartEvent.RemoveItem -> {viewModel.removeItemFromCart(it.item, it.qut)}
            }
        }
    }


    override fun onBackPressed() {
        supportFragmentManager.findFragmentByTag("search_fragment")?.let {
            viewModel.itemsSearchFor.value = listOf()
        }

        super.onBackPressed()
    }


    private fun startFragment() {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_layout, RestaurantFragment(), "restaurant_fragment")
            commit()
        }
    }

}