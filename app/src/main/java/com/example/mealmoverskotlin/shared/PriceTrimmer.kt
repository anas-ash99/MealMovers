package com.example.mealmoverskotlin.shared

object PriceTrimmer {

    fun trim(price:Double):String{
        val index:Int = price.toString().indexOf(".")


        if (price.toString().length < index + 3){

            return  "${price.toString().substring(0,index + 2)}0"
        }else{
            return  price.toString().substring(0,index + 3)
        }
    }


}