package com.jminnovatech.mprint.data

import com.jminnovatech.core.model.BaseResponse
import com.jminnovatech.core.model.LoginRequest
import com.jminnovatech.core.model.LoginResponse
import com.jminnovatech.core.network.RetrofitClient
import com.jminnovatech.core.repository.BaseRepository
import com.jminnovatech.core.utils.Resource
import com.jminnovatech.mprint.data.model.ProfileResponse

class Repository : BaseRepository() {

    // 🔐 LOGIN
    suspend fun login(body: LoginRequest): Resource<LoginResponse> {
        return safeApiCall {
            RetrofitClient.api.login(body)
        }
    }

    // 📍 CHECK-IN
    suspend fun checkIn(token: String, lat: String, long: String)
            : Resource<BaseResponse<Any>> {

        return safeApiCall {
            RetrofitClient.api.checkIn(
                "Bearer $token",
                mapOf("lat" to lat, "long" to long)
            )
        }
    }

    suspend fun checkOut(token: String, lat: String, long: String)
            : Resource<BaseResponse<Any>> {

        return safeApiCall {
            RetrofitClient.api.checkOut(
                "Bearer $token",
                mapOf("lat" to lat, "long" to long)
            )
        }
    }
    suspend fun getProfile(token: String): Resource<ProfileResponse> {
        return safeApiCall {
            RetrofitClient.api.profile("Bearer $token")
        }
    }
    suspend fun logout(token: String): Resource<BaseResponse<Any>> {
        return safeApiCall {
            RetrofitClient.api.logout("Bearer $token")
        }
    }
}