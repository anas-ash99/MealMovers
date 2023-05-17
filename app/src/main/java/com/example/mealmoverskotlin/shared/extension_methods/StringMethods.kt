package com.example.mealmoverskotlin.shared.extension_methods

object StringMethods {



    fun String.shortName(): String{
        if(this.isEmpty()){
            return ""
        }


        val words = this.split(" ")
        if (words.size == 1){
            return "${words[0].substring(0,1).uppercase()}${words[0].substring(1,2).uppercase()}"
        }

        if (words.size == 2){

            return words[0].substring(0,1).uppercase() + words[1].substring(0,1).uppercase()
        }

        if (words.size == 3){
            return words[0].substring(0,1).uppercase() + words[2].substring(0,1).uppercase()
        }
        return ""

    }


}