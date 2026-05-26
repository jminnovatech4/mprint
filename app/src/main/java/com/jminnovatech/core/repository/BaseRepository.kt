package com.jminnovatech.core.repository

import android.util.Log
import com.jminnovatech.core.model.ApplyLeaveRequest
import com.jminnovatech.core.model.ApproveLeaveRequest
import com.jminnovatech.core.model.BackToBaseRequest
import com.jminnovatech.core.model.CompleteTaskRequest
import com.jminnovatech.core.model.ReachClientRequest
import com.jminnovatech.core.model.StartTaskRequest
import com.jminnovatech.core.network.RetrofitClient
import com.jminnovatech.core.utils.Resource
import retrofit2.Response

open class BaseRepository {

    suspend fun <T> safeApiCall(apiCall: suspend () -> Response<T>): Resource<T> {
        try {
            val response = apiCall()

            val errorBodyString = response.errorBody()?.string()  // 🔥 ONLY ONCE

            Log.d("API_DEBUG", "Code = ${response.code()}")
            Log.d("API_DEBUG", "Error = $errorBodyString")

            if (response.code() == 401) {
                return Resource.Error("SESSION_EXPIRED")
            }

            if (response.isSuccessful) {
                return Resource.Success(response.body()!!)
            }

            return Resource.Error(errorBodyString ?: "Something went wrong")

        } catch (e: Exception) {
            return Resource.Error(e.message ?: "Error")
        }
    }
    suspend fun leaveTypes(token: String) =
        safeApiCall {
            RetrofitClient.api.leaveTypes("Bearer $token")
        }

    suspend fun applyLeave(
        token: String,
        body: ApplyLeaveRequest
    ) = safeApiCall {
        RetrofitClient.api.applyLeave(
            "Bearer $token",
            body
        )
    }

    suspend fun myLeaves(token: String) =
        safeApiCall {
            RetrofitClient.api.myLeaves("Bearer $token")
        }

    suspend fun managerLeaves(token: String) =
        safeApiCall {
            RetrofitClient.api.managerLeaves("Bearer $token")
        }

    suspend fun approveLeave(
        token: String,
        body: ApproveLeaveRequest
    ) = safeApiCall {
        RetrofitClient.api.approveLeave(
            "Bearer $token",
            body
        )
    }


    // ===============================
// COMPLAINT LIST
// ===============================

    suspend fun complaintList(
        token: String
    ) = safeApiCall {

        RetrofitClient.api.complaintList(
            "Bearer $token"
        )
    }

// ===============================
// MY TASKS
// ===============================

    suspend fun myTasks(
        token: String
    ) = safeApiCall {

        RetrofitClient.api.myTasks(
            "Bearer $token"
        )
    }

// ===============================
// START TASK
// ===============================

    suspend fun startTask(

        token: String,

        body: StartTaskRequest

    ) = safeApiCall {

        RetrofitClient.api.startTask(

            "Bearer $token",

            body
        )
    }

// ===============================
// REACH CLIENT
// ===============================

    suspend fun reachClient(

        token: String,

        body: ReachClientRequest

    ) = safeApiCall {

        RetrofitClient.api.reachClient(

            "Bearer $token",

            body
        )
    }

// ===============================
// COMPLETE TASK
// ===============================

    suspend fun completeTask(

        token: String,

        body: CompleteTaskRequest

    ) = safeApiCall {

        RetrofitClient.api.completeTask(

            "Bearer $token",

            body
        )
    }

// ===============================
// BACK TO BASE
// ===============================

    suspend fun backToBase(

        token: String,

        body: BackToBaseRequest

    ) = safeApiCall {

        RetrofitClient.api.backToBase(

            "Bearer $token",

            body
        )
    }

// ===============================
// COMPLETED TASKS
// ===============================

    suspend fun completedTasks(
        token: String
    ) = safeApiCall {

        RetrofitClient.api.completedTasks(
            "Bearer $token"
        )
    }
}