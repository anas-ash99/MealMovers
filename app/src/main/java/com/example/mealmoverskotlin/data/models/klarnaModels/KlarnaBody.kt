package com.example.mealmoverskotlin.data.models.klarnaModels

data class KlarnaBody(
    var intent: String = "buy",
    var locale: String = "en-US",
    var purchase_country: String = "DE",
    var purchase_currency: String = "EUR",
    var order_amount: Int = 10,
    var order_lines: MutableList<OrderLine> = mutableListOf(),
    var order_tax_amount: Int = 0,


)