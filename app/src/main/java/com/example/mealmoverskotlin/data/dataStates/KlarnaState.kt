package com.example.mealmoverskotlin.data.dataStates

sealed class KlarnaState{
    object PAID: KlarnaState()
    object CANCELED: KlarnaState()
    object LOADING: KlarnaState()
    object VIEW_LAODING_DONE: KlarnaState()
    data class Error(val exception: Exception): KlarnaState()
}
