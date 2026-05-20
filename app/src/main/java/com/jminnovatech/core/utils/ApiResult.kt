package com.jminnovatech.core.utils

data class ApiResult<T>(
    val status: Boolean,
    val message: String?,
    val data: T?
)