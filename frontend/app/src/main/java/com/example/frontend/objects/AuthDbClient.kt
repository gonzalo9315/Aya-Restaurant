package com.example.frontend.objects

import com.example.frontend.interfaces.AuthService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object AuthDbClient {
    private val retrofit =  Retrofit.Builder()
        .baseUrl("http://10.0.2.2:5001/api/Auth/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val service = retrofit.create(AuthService::class.java)
}