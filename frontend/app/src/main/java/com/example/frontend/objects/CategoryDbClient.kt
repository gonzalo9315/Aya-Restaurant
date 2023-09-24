package com.example.frontend.objects

import com.example.client.ui.interfaces.CategoryService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object CategoryDbClient {

    private val retrofit =  Retrofit.Builder()
            .baseUrl("http://10.0.2.2:5001/api/Categories/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    val service = retrofit.create(CategoryService::class.java)
}