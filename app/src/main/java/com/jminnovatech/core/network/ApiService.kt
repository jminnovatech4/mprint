package com.jminnovatech.core.network

import com.jminnovatech.core.model.ApplyLeaveRequest
import com.jminnovatech.core.model.ApproveLeaveRequest
import com.jminnovatech.core.model.BackToBaseRequest
import com.jminnovatech.core.model.BaseResponse
import com.jminnovatech.core.model.CancelTaskRequest
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
import com.jminnovatech.core.model.ComplaintListResponse
import com.jminnovatech.core.model.CompleteTaskRequest
import com.jminnovatech.core.model.ReachClientRequest
import com.jminnovatech.core.model.StartTaskRequest
import com.jminnovatech.core.model.TaskResponse
import okhttp3.MultipartBody
import retrofit2.http.Multipart
import retrofit2.http.Part

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




    // ===============================
// TASK LIST
// ===============================

    @POST("api/api/complaint-list")
    suspend fun complaintList(

        @Header("Authorization")
        token: String

    ): Response<ComplaintListResponse>

// ===============================
// MY TASKS
// ===============================

    @POST("api/api/my-tasks")
    suspend fun myTasks(

        @Header("Authorization")
        token: String

    ): Response<ComplaintListResponse>

// ===============================
// START TASK
// ===============================

    @POST("api/api/start-task")
    suspend fun startTask(

        @Header("Authorization")
        token: String,

        @Body body: StartTaskRequest

    ): Response<BaseResponse<Any>>

// ===============================
// REACH CLIENT
// ===============================

    @POST("api/api/reach-client")
    suspend fun reachClient(

        @Header("Authorization")
        token: String,

        @Body body: ReachClientRequest

    ): Response<BaseResponse<Any>>

// ===============================
// COMPLETE TASK
// ===============================

    @POST("api/api/complete-task")
    suspend fun completeTask(

        @Header("Authorization")
        token: String,

        @Body body: CompleteTaskRequest

    ): Response<BaseResponse<Any>>

// ===============================
// BACK TO BASE
// ===============================

    @POST("api/api/back-to-base")
    suspend fun backToBase(

        @Header("Authorization")
        token: String,

        @Body body: BackToBaseRequest

    ): Response<BaseResponse<Any>>

// ===============================
// COMPLETED TASKS
// ===============================

    @POST("api/api/completed-tasks")
    suspend fun completedTasks(

        @Header("Authorization")
        token: String

    ): Response<ComplaintListResponse>

    @Multipart
    @POST("api/api/upload-signature")
    suspend fun uploadSignature(

        @Header("Authorization")
        token: String,

        @Part
        signature: MultipartBody.Part

    ): Response<Map<String, Any>>


    @POST("api/api/cancel-task")
    suspend fun cancelTask(

        @Header("Authorization")
        token: String,

        @Body
        body: CancelTaskRequest

    ): Response<TaskResponse>

    @POST("api/api/task-history")
    suspend fun taskHistory(

        @Header("Authorization")
        token: String

    ): Response<ComplaintListResponse>
}