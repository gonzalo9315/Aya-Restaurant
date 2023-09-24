package com.example.frontend.interfaces

import com.example.frontend.models.DishAmount
import com.example.frontend.models.DishCart
import com.example.frontend.models.Order
import com.example.frontend.models.OrderItem
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface OrderService {

    @Headers("Content-Type: application/json")
    @POST
    fun createOrder(@Header("Authorization") token: String?, @Body order: Order): Call<Order>

    @GET("NewOrders")
    fun getAllNewOrders(@Header("Authorization") token: String): Call<List<Order>>

    @GET("MyOrders")
    fun getMyOrders(@Header("Authorization") token: String): Call<List<Order>>

    @GET("MyNewOrder")
    fun getMyNewOrder(@Header("Authorization") token: String?): Call<Order>

    @GET("{id}/Items")
    fun getAllItemsOfOrder(@Header("Authorization") token: String?, @Path("id") id: Int?): Call<List<DishCart>>

    @GET("{id}")
    fun getOrderByID(@Header("Authorization") token: String?, @Path("id") id: Int?): Call<Order>


    @POST("AddItem")
    fun addItemToOrder(@Header("Authorization") token: String?, @Body orderItem: OrderItem): Call<Boolean>

    @PUT("{id}")
    fun updateOrder(@Header("Authorization") token: String?, @Body order: Order?, @Path("id") id: Int?): Call<Boolean>
}