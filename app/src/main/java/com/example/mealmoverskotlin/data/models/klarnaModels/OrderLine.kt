package com.example.mealmoverskotlin.data.models.klarnaModels

data class OrderLine(
    var image_url: String = "https://www.exampleobjects.com/logo.png",
    var name: String = "Pizza",
    var product_url: String = "https://www.estore.com/products/f2a8d7e34",
    var quantity: Int = 1,
    var reference: String = "",
    var tax_rate: Int = 0,
    var total_amount: Int = 10,
    var total_discount_amount: Int = 0,
    var total_tax_amount: Int = 0,
    var type: String = "physical",
    var unit_price: Int = 10
)