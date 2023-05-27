package com.example.mealmoverskotlin.data.models

import androidx.lifecycle.MutableLiveData

data class FilterDialogModel(
    var filterItems: MutableLiveData<MutableList<String>> = MutableLiveData(),
    var sortType:String= "recommended"
)
