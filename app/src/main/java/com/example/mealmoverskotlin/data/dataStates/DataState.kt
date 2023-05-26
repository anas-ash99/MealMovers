package com.example.mealmoverskotlin.data.dataStates

sealed class DataState<out R>{

    data class Success<out T>(val data:T): DataState<T>()
    data class Error(val exception: Exception): DataState<Nothing>()
    object Loading: DataState<Nothing>()

}



//sealed class DataState<T>(val data:T? = null, val message:String?= null){
//
//    class Success<T>( data:T):DataState<T>()
//    class Error<T>(message: String?, data: T? = null): DataState<T>(data, message)
//    class Loading<T>(data: T? = null):DataState<T>(data)
//
//}
