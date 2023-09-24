package com.example.frontend.objects

import com.example.frontend.interfaces.UserService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object UserDbClient {
    private val retrofit =  Retrofit.Builder()
        .baseUrl("http://10.0.2.2:5001/api/Users/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val service = retrofit.create(UserService::class.java)
}