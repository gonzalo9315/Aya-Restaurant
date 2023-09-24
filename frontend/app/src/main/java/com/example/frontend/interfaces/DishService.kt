package com.example.client.ui.interfaces

import com.example.frontend.models.Dish
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface DishService {

    @GET("Category/{id}")
    fun getDishesByCategory(@Path("id") id: Int?): Call<List<Dish>>

    @GET("{id}")
    fun getDishByID(@Path("id") id: Int?): Call<Dish>
}