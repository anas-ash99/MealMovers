package com.example.mealmoverskotlin.data.models

data class StripeResponse(
    val customer: String = "",
    val ephemeralKey: String = "",
    val paymentIntent: String = "",
    val publishableKey: String = ""
)