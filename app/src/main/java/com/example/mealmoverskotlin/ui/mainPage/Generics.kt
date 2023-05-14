package com.example.mealmoverskotlin.ui.mainPage

class Generics<T>(
    data:T
) {
    val data:T = data
    fun printData(){
        println(data)


    }


}