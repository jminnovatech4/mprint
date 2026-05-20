package com.jminnovatech.core.model

data class LoginResponse(
    val status: Boolean,
    val token: String,
    val user: User,
    val message: String?,
)

data class User(
    val employee_id: Int,
    val name: String
)