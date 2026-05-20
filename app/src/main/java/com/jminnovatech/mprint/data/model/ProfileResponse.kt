package com.jminnovatech.mprint.data.model

data class ProfileResponse(
    val status: Boolean,
    val data: ProfileData
)

data class ProfileData(
    val name: String,
    val employee_id: String,
    val designation: String,
    val mobile: String,
    val email: String,
    val office_lat: String,
    val office_long: String,
    val present_count: Int,
    val leave_count: Int,
    val today_attendance: TodayAttendance?
)

data class TodayAttendance(
    val attendance_in_time: String?,
    val attendance_out_time: String?
)