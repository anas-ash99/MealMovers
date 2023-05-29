package com.example.mealmoverskotlin.data.events

sealed class AddRestaurantToFavouritesEvent<out R>{
    data class Success<out T>(val data:T): AddRestaurantToFavouritesEvent<T>()
    data class Error(val exception: Exception): AddRestaurantToFavouritesEvent<Nothing>()
    object Loading: AddRestaurantToFavouritesEvent<Nothing>()

}