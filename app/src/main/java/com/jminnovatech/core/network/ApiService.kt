package com.jminnovatech.core.network

import com.jminnovatech.core.model.ApplyLeaveRequest
import com.jminnovatech.core.model.ApproveLeaveRequest
import com.jminnovatech.core.model.BaseResponse
import com.jminnovatech.core.model.LeaveTypeResponse
import com.jminnovatech.core.model.LoginRequest
import com.jminnovatech.core.model.LoginResponse
import com.jminnovatech.core.model.MyLeavesResponse
import com.jminnovatech.mprint.data.model.ProfileResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiService {
    @POST("api/api/login")
    suspend fun login(
        @Body body: LoginRequest
    ): Response<LoginResponse>
    @POST("api/api/check-in")
    suspend fun checkIn(
        @Header("Authorization") token: String,
        @Body body: Map<String, String>
    ): Response<BaseResponse<Any>>

    @POST("api/api/check-out")
    suspend fun checkOut(
        @Header("Authorization") token: String,
        @Body body: Map<String, String>
    ): Response<BaseResponse<Any>>

    @POST("api/api/profile")
    suspend fun profile(
        @Header("Authorization") token: String
    ): Response<ProfileResponse>

    @POST("api/api/logout")
    suspend fun logout(
        @Header("Authorization") token: String
    ): Response<BaseResponse<Any>>

    @POST("api/api/leave-types")
    suspend fun leaveTypes(
        @Header("Authorization") token: String
    ): Response<LeaveTypeResponse>

    @POST("api/api/apply-leave")
    suspend fun applyLeave(
        @Header("Authorization") token: String,
        @Body body: ApplyLeaveRequest
    ): Response<BaseResponse<Any>>

    @POST("api/api/my-leaves")
    suspend fun myLeaves(
        @Header("Authorization") token: String
    ): Response<MyLeavesResponse>

    @POST("api/api/manager-leaves")
    suspend fun managerLeaves(
        @Header("Authorization") token: String
    ): Response<MyLeavesResponse>

    @POST("api/api/approve-leave")
    suspend fun approveLeave(
        @Header("Authorization") token: String,
        @Body body: ApproveLeaveRequest
    ): Response<BaseResponse<Any>>
}