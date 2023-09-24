package com.example.frontend.interfaces

import com.example.frontend.models.LoginResponse
import com.example.frontend.models.User
import com.example.frontend.models.UserLogin
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT

interface AuthService {

    @GET("GetMyProfile")
    fun getMyInfo(@Header("Authorization") token: String?): Call<User>

    @POST("Login")
    fun login(@Body user : UserLogin): Call<LoginResponse>

    @POST("Register")
    fun register(@Body user: User): Call<Boolean>

    @PUT("UpdateMyData")
    fun updateInfo(@Header("Authorization") token: String?, @Body user: User): Call<Boolean>
}