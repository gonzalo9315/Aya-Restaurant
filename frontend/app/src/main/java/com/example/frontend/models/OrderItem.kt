package com.example.frontend.models

data class OrderItem(
    val amount: Int,
    val dishId: Int?,
    val orderId: Int?
)