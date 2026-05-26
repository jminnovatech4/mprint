package com.jminnovatech.core.model

// ===============================
// COMMON RESPONSE
// ===============================

data class TaskResponse(

    val status: Boolean,

    val msg: String?
)

// ===============================
// TASK LIST RESPONSE
// ===============================

data class ComplaintListResponse(

    val status: Boolean,

    val data: List<TaskItem>
)

// ===============================
// TASK ITEM
// ===============================

data class TaskItem(

    val sl: Int,

    val complain_id: String,

    val complain_datetime: String?,

    val client_name: String?,

    val model: String?,

    val assigned_employee_id: String?,

    val assigned_name: String?,

    val designation: String?,

    val mobile_no: String?,

    val mode_of_transport: String?,

    val start_base: String?,

    val reach_client: String?,

    val complete_work: String?,

    val back_to_base: String?,

    val start_odo: String?,

    val reach_odo: String?,

    val back_to_base_odo: String?,

    val traveling_cost: String?,

    val image_link: String?,

    val remarks: String?,

    val web_assigne_by: String?,

    val web_assigne_time: String?,

    val start_lat: String?,

    val start_long: String?,

    val reach_lat: String?,

    val reach_long: String?,

    val complete_lat: String?,

    val complete_long: String?,

    val back_lat: String?,

    val back_long: String?,

    val task_status: String?,

    val signature_link: String?
)

// ===============================
// START TASK
// ===============================

data class StartTaskRequest(

    val task_id: Int,

    val complain_id: String,

    val mode_of_transport: String,

    val start_odo: String,

    val lat: String,

    val long: String
)

// ===============================
// REACHED CLIENT
// ===============================

data class ReachClientRequest(

    val task_id: Int,

    val reach_odo: String,

    val lat: String,

    val long: String
)

// ===============================
// COMPLETE TASK
// ===============================

data class CompleteTaskRequest(

    val task_id: Int,

    val complain_id: String,

    val remarks: String,

    val image_link: String,

    val signature_link: String,

    val lat: String,

    val long: String
)

// ===============================
// BACK TO BASE
// ===============================

data class BackToBaseRequest(

    val task_id: Int,

    val end_odo: String,

    val travel_cost: String,

    val lat: String,

    val long: String
)