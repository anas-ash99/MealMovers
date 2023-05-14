package com.example.mealmoverskotlin.data.models.klarnaModels

data class KlarnaResponse(
    var client_token: String? = null,
    var payment_method_categories: List<PaymentMethodCategory?>? = null,
    var session_id: String? = null,
    var error_code:String? = null
)