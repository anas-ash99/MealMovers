package com.example.mealmoverskotlin.domain.dialogs

import android.annotation.SuppressLint

import android.view.Gravity
import android.view.View
import android.widget.GridView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mealmoverskotlin.R
import com.example.mealmoverskotlin.domain.adapters.DialogSortItemsAdapter
import com.example.mealmoverskotlin.domain.adapters.FilterGridViewAdapter
import com.example.mealmoverskotlin.domain.viewModels.MainPageViewModel
import com.example.mealmoverskotlin.ui.mainPage.MainActivity


@SuppressLint("SetTextI18n")
class RestaurantsFilterDialog(
    private val activity:MainActivity,
    private val viewModel:MainPageViewModel,

):BaseDialog(activity, Gravity.BOTTOM, R.layout.dialog_filter_restaurants ) {

    var sortItem:MutableLiveData<String> = MutableLiveData("Recommended")
    var filterItems:MutableLiveData<MutableList<String>> = MutableLiveData(mutableListOf())
    private val gridViewAdapter = FilterGridViewAdapter(activity, this)
    private val rvAdapter = DialogSortItemsAdapter(activity,this)
    private val gridView: GridView = dialog.findViewById(R.id.gridView)
    private val recyclerView: RecyclerView = dialog.findViewById(R.id.RV_sortItems)
    private val arrowIcon:CardView = dialog.findViewById(R.id.arrow_icon)
    private val button:CardView = dialog.findViewById(R.id.button)
    private val buttonTV:TextView = dialog.findViewById(R.id.buttonTV)
    private val clearFiltersButton:TextView = dialog.findViewById(R.id.clearFiltersButton)

    init {
        initGridView()
        onArrowCloseClick()
        initRecyclerView()
        observeSortItem()
        observeFilterItems()
        onClearFiltersButtonClick()
        onApplyButtonClick()

    }

    private fun initRecyclerView(){
        recyclerView.adapter = rvAdapter
        recyclerView.layoutManager =LinearLayoutManager(activity,RecyclerView.HORIZONTAL,false)
    }

    private fun onArrowCloseClick(){
       arrowIcon.setOnClickListener {
           dialog.dismiss()
       }
    }


    fun onSelectSortItem(item:String){

        sortItem.value = item


    }


    private fun observeSortItem(){
        sortItem.observe(activity){
            if (it != "Recommended"){
                clearFiltersButton.visibility = View.VISIBLE
                changeToDoneButton()
            }else{

                if (filterItems.value?.isEmpty()!!){
                    changeToCancelButton()
                    clearFiltersButton.visibility = View.INVISIBLE

                }
                rvAdapter.onClearFilterClick()
                recyclerView.scrollToPosition(0)

            }
        }
    }



    private fun observeFilterItems(){
        filterItems.observe(activity){
                if (it.isNotEmpty()){
                    changeToDoneButton()
                    clearFiltersButton.visibility =View.VISIBLE
                }else{
                    if (sortItem.value == "Recommended"){
                        clearFiltersButton.visibility =View.INVISIBLE
                        changeToCancelButton()
                    }
                }
        }
    }

    private fun onClearFiltersButtonClick(){
        clearFiltersButton.setOnClickListener {
            sortItem.value = "Recommended"
            filterItems.value?.removeAll(filterItems.value!!)
            filterItems.value = filterItems.value
            rvAdapter.onClearFilterClick()
            recyclerView.scrollToPosition(0)
            gridViewAdapter.onClearFilterClick()
            gridView.smoothScrollToPosition(0)

        }

    }


     private fun changeToDoneButton(){
         buttonTV.text = "APPLY"
         buttonTV.setTextColor(activity.getColor(R.color.white))
         button.setCardBackgroundColor(activity.getColor(R.color.teal_200))
     }



    private fun changeToCancelButton(){

        if (viewModel.hasSelectedSortItemChanged || viewModel.hasSelectedFilterItemsChanged ){
            changeToDoneButton()

        }else{
            buttonTV.text = "CANCEL"
            buttonTV.setTextColor(activity.getColor(R.color.teal_200))
            button.setCardBackgroundColor(activity.getColor(R.color.item_not_selected))
        }


    }

    private fun onApplyButtonClick(){

        button.setOnClickListener {

            if (buttonTV.text == "APPLY"){
                viewModel.onDialogApplyClick(filterItems.value!!, sortItem.value!!)
            }
            dialog.dismiss()
        }


    }
    private fun initGridView(){
        gridView.adapter = gridViewAdapter
    }
}
