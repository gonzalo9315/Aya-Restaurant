package com.example.client.ui.interfaces

import com.example.frontend.models.Category
import retrofit2.Call
import retrofit2.http.GET

interface CategoryService {

    @GET("Avalaible")
    fun getCategories(): Call<List<Category>>
}