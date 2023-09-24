package com.example.frontend.objects

import com.example.client.ui.interfaces.DishService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object DishesDbClient {

    private val retrofit =  Retrofit.Builder()
        .baseUrl("http://10.0.2.2:5001/api/Dishes/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val service = retrofit.create(DishService::class.java)
}