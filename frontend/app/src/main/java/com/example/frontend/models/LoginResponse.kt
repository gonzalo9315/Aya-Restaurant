package com.example.frontend.models

class LoginResponse (
    val token: String,
    val username: String,
    val role: Int = 0
)