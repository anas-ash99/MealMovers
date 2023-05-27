package com.example.mealmoverskotlin.domain.dialogs

import android.annotation.SuppressLint

import android.view.Gravity
import android.view.View
import android.widget.GridView
import android.widget.TextView
import android.widget.Toast
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

    private var rvAdapter:DialogSortItemsAdapter? = null
    private val gridView: GridView = dialog.findViewById(R.id.gridView)
    private val recyclerView: RecyclerView = dialog.findViewById(R.id.RV_sortItems)
    private val arrowIcon:CardView = dialog.findViewById(R.id.arrow_icon)
    private val button:CardView = dialog.findViewById(R.id.button)
    private val buttonTV:TextView = dialog.findViewById(R.id.buttonTV)
    private val clearFiltersButton:TextView = dialog.findViewById(R.id.clearFiltersButton)
    private var gridViewAdapter:FilterGridViewAdapter? = null
    private var isDismissedViaButton = false
//
//    init {
//        initGridView()
//        onArrowCloseClick()
//        initRecyclerView()
//        observeSortItem()
//        observeFilterItems()
//        onClearFiltersButtonClick()
//        onApplyButtonClick()
//
//    }


    fun showDialog(){

        initGridView()
        onArrowCloseClick()
        initRecyclerView()
        observeSortItem()
        observeFilterItems()
        onClearFiltersButtonClick()
        onApplyButtonClick()
         onDismissListener()

////        println(viewModel.selectedItems)
//        println("         start        ")
//        println("view " + viewModel.selectedItems)
//        println("filter 1  " + filterItems.value)
        if (viewModel.selectedItems == filterItems.value){

            buttonTV.text = "CANCEL"
            buttonTV.setTextColor(activity.getColor(R.color.teal_200))
            button.setCardBackgroundColor(activity.getColor(R.color.item_not_selected))
        }else{

        }
        dialog.show()
    }

    private fun initRecyclerView(){
        rvAdapter = DialogSortItemsAdapter(activity, this, viewModel.sortTypeVM)
        recyclerView.adapter = rvAdapter
        recyclerView.layoutManager =LinearLayoutManager(activity, RecyclerView.HORIZONTAL,false)
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


                if (viewModel.sortTypeVM != it){

                    changeToDoneButton()
                }else if (filterItems.value?.isEmpty()!!){

                    changeToCancelButton()

                    clearFiltersButton.visibility = View.INVISIBLE
                }

                rvAdapter?.onClearFilterClick()
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
                    if (sortItem.value == "Recommended" && viewModel.selectedItems == filterItems.value ){
                        clearFiltersButton.visibility =View.INVISIBLE
                        changeToCancelButton()
                    }else{
                        changeToDoneButton()
                    }
                }
        }
    }

    private fun onClearFiltersButtonClick(){
        clearFiltersButton.setOnClickListener {
            sortItem.value = "Recommended"
            filterItems.value?.removeAll(filterItems.value!!)
            filterItems.value = filterItems.value
            rvAdapter?.onClearFilterClick()
            recyclerView.scrollToPosition(0)
            gridViewAdapter?.onClearFilterClick()
            gridView.smoothScrollToPosition(0)

        }

    }


     private fun changeToDoneButton(){
         buttonTV.text = "APPLY"
         buttonTV.setTextColor(activity.getColor(R.color.white))
         button.setCardBackgroundColor(activity.getColor(R.color.teal_200))
     }



    private fun changeToCancelButton(){

        if (viewModel.selectedItems != filterItems.value ){
            changeToDoneButton()

        }else{
            buttonTV.text = "CANCEL"
            buttonTV.setTextColor(activity.getColor(R.color.teal_200))
            button.setCardBackgroundColor(activity.getColor(R.color.item_not_selected))
        }


    }

    private fun onDismissListener(){

        dialog.setOnDismissListener {
            if (buttonTV.text == "APPLY"){

                if (!isDismissedViaButton){

                    if (viewModel.selectedItems != filterItems.value){
                        filterItems.value?.removeAll(filterItems.value!!)
                        viewModel.selectedItems.forEach {
                            filterItems.value?.add(it)
                        }
                    }
                   sortItem.value = viewModel.sortTypeVM
                }
                else{
                    viewModel.selectedItems.removeAll(viewModel.selectedItems)
                    filterItems.value?.onEach {
                        if (!viewModel.selectedItems.contains(it)) viewModel.selectedItems.add(it)
                    }

                }

                viewModel.sortTypeVM = sortItem.value!!
                viewModel.onDialogApplyClick(sortItem.value!!)
            }
            isDismissedViaButton = false
        }
    }

    private fun onApplyButtonClick(){

        button.setOnClickListener {

            isDismissedViaButton = true
            dialog.dismiss()
        }


    }
    private fun initGridView(){
       gridViewAdapter = FilterGridViewAdapter(activity, this, viewModel.selectedItems)
        gridView.adapter = gridViewAdapter

    }
}
