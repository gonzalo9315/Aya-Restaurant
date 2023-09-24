package com.example.frontend.objects

import com.example.frontend.interfaces.OrderService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object OrderDbClient {
    private val retrofit =  Retrofit.Builder()
        .baseUrl("http://10.0.2.2:5001/api/Orders/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val service = retrofit.create(OrderService::class.java)
}