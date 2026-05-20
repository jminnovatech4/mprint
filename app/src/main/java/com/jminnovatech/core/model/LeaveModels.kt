package com.jminnovatech.core.model


data class LeaveTypeResponse(
    val status: Boolean,
    val data: List<LeaveType>
)

data class LeaveType(
    val id: String,
    val name: String,
    val available: Int,
    val used: Int,
    val total: Int
)

data class ApplyLeaveRequest(
    val leave_type: String,
    val from_date: String,
    val to_date: String,
    val reason: String
)

data class MyLeavesResponse(
    val status: Boolean,
    val data: List<LeaveItem>
)

data class LeaveItem(
    val id: Int,
    val leave_type: String,
    val from_date: String,
    val to_date: String,
    val reason: String?,
    val status: String,
    val days: Int
)

data class ApproveLeaveRequest(
    val leave_id: Int,
    val action: String
)