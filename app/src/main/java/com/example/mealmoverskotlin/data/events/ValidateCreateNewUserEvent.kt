package com.example.mealmoverskotlin.data.events

enum class ValidateCreateNewUserEvent( val message:String ){
    PASSWORDS_DONT_MATCH("Passwords don't match"),
    EMAIL_NOT_VALID("Email you entered is not valid"),
    EMAIL_IS_VALID("Email is valid"),

}