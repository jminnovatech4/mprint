package com.jminnovatech.core.model

data class BaseResponse<T>(
    val status: Boolean,
    val msg: String?,
    val data: T?
)