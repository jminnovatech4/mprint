package com.jminnovatech.mprint.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jminnovatech.core.model.ApplyLeaveRequest
import com.jminnovatech.core.model.BaseResponse
import com.jminnovatech.core.model.LeaveTypeResponse
import com.jminnovatech.core.model.LoginRequest
import com.jminnovatech.core.model.LoginResponse
import com.jminnovatech.core.model.MyLeavesResponse
import com.jminnovatech.core.utils.Resource
import com.jminnovatech.mprint.data.Repository
import com.jminnovatech.mprint.data.model.ProfileResponse
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class MainViewModel : ViewModel() {

    private val repo = Repository()


    var attendanceState by mutableStateOf<Resource<BaseResponse<Any>>?>(null)
        private set

    // 🔐 LOGIN
    var loginState by mutableStateOf<Resource<LoginResponse>?>(null)
        private set

    fun login(id: String, pass: String, deviceId: String) {
        viewModelScope.launch {

            loginState = Resource.Loading()

            loginState = repo.login(
                LoginRequest(
                    login_id = id,
                    password = pass,
                    device_id = deviceId
                )
            )
        }
    }

    // 📍 CHECK-IN
    fun checkIn(token: String, lat: String, long: String) {
        viewModelScope.launch {
            attendanceState = Resource.Loading()
            attendanceState = repo.checkIn(token, lat, long)
        }
    }

    // 📍 CHECK-OUT
    fun checkOut(token: String, lat: String, long: String) {
        viewModelScope.launch {
            attendanceState = Resource.Loading()
            attendanceState = repo.checkOut(token, lat, long)
        }
    }
    fun clearState() {
        attendanceState = null
    }

    var checkInTime by mutableStateOf<Long?>(null)
        private set

    fun startCheckIn() {
        checkInTime = System.currentTimeMillis()
    }

    fun stopCheckIn() {
        checkInTime = null
    }

    var profileState by mutableStateOf<Resource<ProfileResponse>?>(null)
        private set

    fun loadProfile(token: String) {
        viewModelScope.launch {
            profileState = Resource.Loading()
            profileState = repo.getProfile(token)
        }
    }
    fun setCheckInFromServer(time: String) {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        checkInTime = sdf.parse(time)?.time
    }
    fun clearCheckIn() {
        checkInTime = null
    }
    var logoutState by mutableStateOf<Resource<BaseResponse<Any>>?>(null)
        private set

    fun logout(token: String) {
        viewModelScope.launch {
            logoutState = Resource.Loading()
            logoutState = repo.logout(token)
        }
    }
    fun refreshAll(token: String) {

        // 🔥 profile reload
        loadProfile(token)

        // 🔥 attendance reset (optional)
        clearState()

        // 🔥 timer reset (optional)
        clearCheckIn()
    }
    var isActionLoading by mutableStateOf(false)
        private set

    fun startLoading() {
        isActionLoading = true
    }

    fun stopLoading() {
        isActionLoading = false
    }
    var leaveTypesState by mutableStateOf<Resource<LeaveTypeResponse>?>(null)
        private set

    var applyLeaveState by mutableStateOf<Resource<BaseResponse<Any>>?>(null)
        private set

    var myLeavesState by mutableStateOf<Resource<MyLeavesResponse>?>(null)
        private set

    fun loadLeaveTypes(token: String) {
        viewModelScope.launch {

            leaveTypesState = Resource.Loading()

            leaveTypesState = repo.leaveTypes(token)
        }
    }

    fun applyLeave(
        token: String,
        body: ApplyLeaveRequest
    ) {
        viewModelScope.launch {

            applyLeaveState = Resource.Loading()

            applyLeaveState =
                repo.applyLeave(token, body)
        }
    }

    fun loadMyLeaves(token: String) {
        viewModelScope.launch {

            myLeavesState = Resource.Loading()

            myLeavesState = repo.myLeaves(token)
        }
    }

    fun clearLeaveState() {
        applyLeaveState = null
    }
}