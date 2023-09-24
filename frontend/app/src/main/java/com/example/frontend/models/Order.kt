package com.example.frontend.models

data class Order(
    val address: String,

//    val deletedAt: Any,

//    val isDeleted: Boolean,
    var state: String = "string",
    val createdAt: String = "string",
//    val updatedAt: Any,
//    val userId: Int
    val id: Int = 1
)