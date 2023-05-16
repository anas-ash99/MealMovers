package com.example.mealmoverskotlin.shared.extension_methods

object PriceTrimmer {

    fun trim(price:Double):String{
        val index:Int = price.toString().indexOf(".")


        if (price.toString().length < index + 3){

            return  "${price.toString().substring(0,index + 2)}0"
        }else{
            return  price.toString().substring(0,index + 3)
        }
    }


    fun Double.trim1():String{
        val index:Int = this.toString().indexOf(".")

        return if (this.toString().length < index + 3){
            "${this.toString().substring(0,index + 2)}0"
        }else{
            this.toString().substring(0,index + 3)
        }
    }


}