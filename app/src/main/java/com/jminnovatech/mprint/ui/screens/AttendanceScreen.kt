package com.jminnovatech.mprint.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.material3.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.jminnovatech.mprint.viewmodel.MainViewModel
import com.jminnovatech.core.utils.Resource
import com.jminnovatech.mprint.ui.components.Loader
import com.jminnovatech.mprint.ui.components.SuccessDialog
import com.jminnovatech.core.datastore.SessionManager
import com.jminnovatech.mprint.utils.LocationHelper
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*

import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import com.jminnovatech.mprint.data.model.ProfileResponse
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import com.jminnovatech.core.model.CompleteTaskRequest
import com.jminnovatech.mprint.ui.components.TaskCard
import com.jminnovatech.mprint.ui.components.TaskDetailsDialog
import com.jminnovatech.core.model.TaskItem
import com.jminnovatech.mprint.ui.components.TransportDialog

import com.jminnovatech.core.model.StartTaskRequest
import com.jminnovatech.mprint.ui.components.AttendanceUI
import com.jminnovatech.mprint.ui.components.CompleteWorkDialog
import com.jminnovatech.mprint.ui.components.TaskDetailsDialog
@RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
@Composable
fun AttendanceScreen(vm: MainViewModel, navController: NavHostController) {

    val context = LocalContext.current
    val session = SessionManager(context)
    var currentLat by remember { mutableStateOf(0.0) }
    var currentLong by remember { mutableStateOf(0.0) }
    var distanceText by remember { mutableStateOf("--") }

    var isLocationLoading by remember {
        mutableStateOf(true)
    }

    var isGpsEnabled by remember {
        mutableStateOf(true)
    }
    val locationHelper = LocationHelper(context)
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->

        if (granted) {

            // 🔥 permission allow → auto GPS
            locationHelper.getLocation { lat, long ->

                currentLat = lat.toDoubleOrNull() ?: 0.0
                currentLong = long.toDoubleOrNull() ?: 0.0
            }

        } else {
            Toast.makeText(context, "Permission Denied ❌", Toast.LENGTH_SHORT).show()
        }
    }



    val token = session.getToken() ?: ""
    val state = vm.attendanceState
    val complaintState =
        vm.complaintListState
    val isLoading = state is Resource.Loading
    val profileState = vm.profileState
    val officeLat = (profileState as? Resource.Success<ProfileResponse>)
        ?.data?.data?.office_lat?.toDoubleOrNull()

    val officeLong = (profileState as? Resource.Success<ProfileResponse>)
        ?.data?.data?.office_long?.toDoubleOrNull()
    var showConfirm by remember { mutableStateOf(false) }
    var actionType by remember { mutableStateOf("") }



    val checkInTime = vm.checkInTime

    var workingTime by remember { mutableStateOf("00:00:00") }
    var showSuccess by remember { mutableStateOf(false) }
    var showError by remember { mutableStateOf(false) }
    var showWarning by remember { mutableStateOf(false) }
    var selectedTask by remember {

        mutableStateOf<TaskItem?>(null)
    }

    var showTransportDialog by remember {

        mutableStateOf(false)
    }
    var showCompleteDialog by remember {

        mutableStateOf(false)
    }
    var message by remember { mutableStateOf("") }
    var warningMsg by remember { mutableStateOf("") }

    fun getLocationSafe(onResult: (String, String) -> Unit) {
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            locationHelper.getLocation { lat, long ->
                onResult(lat, long)
            }
        } else {
            permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }
    fun checkGps(): Boolean {

        val manager = context.getSystemService(
            android.content.Context.LOCATION_SERVICE
        ) as android.location.LocationManager

        return manager.isProviderEnabled(
            android.location.LocationManager.GPS_PROVIDER
        )
    }
    fun formatDistance(distance: Double): String {

        return if (distance >= 1000) {
            String.format("%.2f km", distance / 1000)
        } else {
            "${distance.toInt()} m"
        }
    }
    fun calculateDistance(
        lat1: Double,
        lon1: Double,
        lat2: Double,
        lon2: Double
    ): Double {

        val R = 6371000
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)

        val a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) *
                Math.cos(Math.toRadians(lat2)) *
                Math.sin(dLon / 2) * Math.sin(dLon / 2)

        val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))

        return R * c
    }


    LaunchedEffect(Unit) {

        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {

            // already granted
            locationHelper.getLocation { lat, long ->

                currentLat = lat.toDoubleOrNull() ?: 0.0
                currentLong = long.toDoubleOrNull() ?: 0.0

                isLocationLoading = false
            }

        } else {

            // ask permission
            permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }
    LaunchedEffect(Unit) {

        while (true) {

            if (
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {

                locationHelper.getLocation { lat, long ->

                    currentLat = lat.toDoubleOrNull() ?: 0.0
                    currentLong = long.toDoubleOrNull() ?: 0.0

                    isLocationLoading = false
                }
            }

            kotlinx.coroutines.delay(10000) // 10 sec
        }
    }
    // ⏱️ TIMER
    LaunchedEffect(Unit) {
        getLocationSafe { lat, long ->
            currentLat = lat.toDoubleOrNull() ?: 0.0
            currentLong = long.toDoubleOrNull() ?: 0.0
        }
    }
    LaunchedEffect(Unit) {
        val token = session.getToken() ?: ""
        if (token.isNotEmpty()) {
            vm.loadProfile(token)
            vm.loadComplaintList(token)
        }
    }
    LaunchedEffect(Unit) {

        isGpsEnabled = checkGps()

        if (!isGpsEnabled) {

            Toast.makeText(
                context,
                "Please Enable GPS",
                Toast.LENGTH_LONG
            ).show()
        }
    }
    LaunchedEffect(currentLat, currentLong, officeLat, officeLong) {

        if (officeLat != null && officeLong != null &&
            currentLat != 0.0 && currentLong != 0.0) {

            val dist = calculateDistance(
                officeLat,
                officeLong,
                currentLat.toDouble(),
                currentLong.toDouble()
            )

            distanceText = formatDistance(dist)
        }
    }
    LaunchedEffect(checkInTime) {
        if (checkInTime != null) {
            while (true) {
                val diff = System.currentTimeMillis() - checkInTime
                val h = diff / (1000 * 60 * 60)
                val m = (diff / (1000 * 60)) % 60
                val s = (diff / 1000) % 60

                workingTime = String.format("%02d:%02d:%02d", h, m, s)
                kotlinx.coroutines.delay(1000)
            }
        }
    }
    fun isWarning(msg: String?): Boolean {
        if (msg == null) return false

        val lower = msg.lowercase()

        return listOf(
            "outside",
            "already",
            "range",
            "not allowed"
        ).any { lower.contains(it) }
    }

    val isCheckedIn = checkInTime != null

    // 🔥 FULLY DONE (CHECKOUT COMPLETE)


    val isCompleted = when (profileState) {
        is Resource.Success -> {
            profileState.data.data.today_attendance?.attendance_out_time != null
        }
        else -> false
    }
    LaunchedEffect(profileState) {

        val outTime = when (profileState) {
            is Resource.Success -> {
                profileState.data.data.today_attendance?.attendance_out_time
            }
            else -> null
        }

        if (!outTime.isNullOrEmpty()) {
            vm.stopCheckIn()   // 🔥 STOP TIMER FROM SERVER DATA
        }
    }


    if (profileState is Resource.Success) {

        val data = (profileState as Resource.Success).data.data   // 🔥 MUST

        AttendanceUI(
            vm = vm,
            complaintState = complaintState,
            token = token,
            name = data.name,
            designation = data.designation,
            empId = data.employee_id.toString(),
            present = data.present_count.toString(),
            leave = data.leave_count.toString(),
            lat = currentLat.toString(),
            long = currentLong.toString(),
            distance = distanceText,
            inTime = data.today_attendance?.attendance_in_time,
            outTime = data.today_attendance?.attendance_out_time,
            workingTime = workingTime,
            isCheckedIn = isCheckedIn,
            isCheckedOut = data.today_attendance?.attendance_out_time != null,
            isActionLoading = vm.isActionLoading,
            isGpsEnabled = isGpsEnabled,
            isLocationLoading = isLocationLoading,
            onCheckIn = {

                if (!isGpsEnabled) {
                    Toast.makeText(
                        context,
                        "Please Enable GPS",
                        Toast.LENGTH_SHORT
                    ).show()

                    return@AttendanceUI
                }

                if (
                    isLocationLoading ||
                    currentLat == 0.0 ||
                    currentLong == 0.0
                ) {

                    Toast.makeText(
                        context,
                        "Fetching location...",
                        Toast.LENGTH_SHORT
                    ).show()

                    return@AttendanceUI
                }

                actionType = "IN"
                showConfirm = true
            },

            onCheckOut = {

                if (!isGpsEnabled) {

                    Toast.makeText(
                        context,
                        "Please Enable GPS",
                        Toast.LENGTH_SHORT
                    ).show()

                    return@AttendanceUI
                }

                if (
                    isLocationLoading ||
                    currentLat == 0.0 ||
                    currentLong == 0.0
                ) {

                    Toast.makeText(
                        context,
                        "Fetching location...",
                        Toast.LENGTH_SHORT
                    ).show()

                    return@AttendanceUI
                }

                actionType = "OUT"
                showConfirm = true
            },
            onLeaveClick = {
                navController.navigate("leave")
            }
        )
    }


// ✅ 🔥 এইটা ADD করো (AttendanceUI এর নিচে)
    if (showConfirm) {
        AlertDialog(
            onDismissRequest = { showConfirm = false },

            title = { Text("Confirm") },

            text = { Text("Attendance $actionType ?") },

            confirmButton = {
                Button(onClick = {
                    vm.startLoading()
                    getLocationSafe { lat, long ->

                        if (actionType == "IN") {
                            vm.checkIn(token, lat, long)
                        } else {
                            vm.checkOut(token, lat, long)
                        }

                        showConfirm = false
                    }

                }) {
                    Text("OK")
                }
            },

            dismissButton = {
                Button(onClick = {
                    showConfirm = false
                }) {
                    Text("Cancel")
                }
            }
        )
    }

    // 🔥 STATE HANDLE
    when (state) {

        is Resource.Loading -> {
            Loader()
        }

        is Resource.Success -> {

            val res = state.data

            LaunchedEffect(res) {
                vm.stopLoading()
                // ✅ IMPORTANT
                vm.clearState()

                if (res.status) {

                    Toast.makeText(context, res.msg ?: "Success", Toast.LENGTH_SHORT).show()

                    // 🔥 refresh profile instantly
                    val token = session.getToken() ?: ""
                    vm.loadProfile(token)

                } else {
                    Toast.makeText(context, res.msg ?: "Failed", Toast.LENGTH_SHORT).show()
                }
            }
        }

        is Resource.Error -> {

            LaunchedEffect(state) {
                vm.stopLoading()
                vm.clearState()   // 🔥 VERY IMPORTANT

                Toast.makeText(context, state.message, Toast.LENGTH_SHORT).show()
            }
        }

        else -> {}
    }
    if (showWarning) {

        Dialog(onDismissRequest = { showWarning = false }) {

            Card(
                shape = RoundedCornerShape(24.dp),
                elevation = CardDefaults.cardElevation(12.dp)
            ) {

                Column(
                    modifier = Modifier
                        .background(Color.White)
                        .padding(20.dp)
                ) {

                    // 🔥 HEADER
                    Row(verticalAlignment = Alignment.CenterVertically) {

                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFFFFF3E0)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.Warning,
                                contentDescription = null,
                                tint = Color(0xFFFF9800)
                            )
                        }

                        Spacer(Modifier.width(10.dp))

                        Text(
                            "Warning",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFFF9800)
                        )
                    }

                    Spacer(Modifier.height(16.dp))

                    // 🔥 MESSAGE
                    Text(
                        warningMsg,
                        fontSize = 15.sp,
                        color = Color.DarkGray
                    )

                    Spacer(Modifier.height(20.dp))

                    // 🔥 BUTTON
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.CenterEnd   // ✅ FIX
                    ) {
                        Button(
                            onClick = {
                                showWarning = false
                                vm.clearState()
                            },
                            shape = RoundedCornerShape(50.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFFF9800)
                            )
                        ) {
                            Text("OK", color = Color.White)
                        }
                    }
                }
            }
        }
    }
    if (showSuccess) {
        SuccessDialog(message) {
            showSuccess = false
            vm.clearState()
        }
    }

    if (showError) {
        AlertDialog(
            onDismissRequest = { showError = false },
            title = { Text("Error ❌") },
            text = { Text(message) },
            confirmButton = {
                Button(onClick = {
                    showError = false
                    vm.clearState()
                }) {
                    Text("OK")
                }
            }
        )
    }
    if (showTransportDialog && selectedTask != null) {

        TransportDialog(

            onDismiss = {

                showTransportDialog = false
            },

            onSubmit = { transport, odo ->

                vm.startTask(

                    token,

                    StartTaskRequest(

                        task_id = selectedTask!!.sl,

                        complain_id =
                            selectedTask!!.complain_id,

                        mode_of_transport =
                            transport,

                        start_odo = odo,

                        lat = currentLat.toString(),

                        long = currentLong.toString()
                    )
                )

                showTransportDialog = false
            }
        )
    }

    when (val startState = vm.startTaskState) {

        is Resource.Loading -> {

            Loader()
        }

        is Resource.Success -> {

            LaunchedEffect(Unit) {

                Toast.makeText(

                    context,

                    startState.data.msg
                        ?: "Started",

                    Toast.LENGTH_SHORT
                ).show()

                vm.loadComplaintList(token)
            }
        }

        is Resource.Error -> {

            LaunchedEffect(Unit) {

                Toast.makeText(

                    context,

                    startState.message
                        ?: "Error",

                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        else -> {}
    }
    when (val reachState = vm.reachClientState) {

        is Resource.Loading -> {

            Loader()
        }

        is Resource.Success -> {

            LaunchedEffect(reachState) {

                Toast.makeText(

                    context,

                    reachState.data.msg
                        ?: "Reached Client",

                    Toast.LENGTH_SHORT
                ).show()

                // 🔥 RELOAD TASKS
                vm.loadComplaintList(token)

                // 🔥 CLOSE DIALOG
                selectedTask = null
            }
        }

        is Resource.Error -> {

            LaunchedEffect(reachState) {

                Toast.makeText(

                    context,

                    reachState.message
                        ?: "Error",

                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        else -> {}
    }

}



@Composable
fun StatCard(title: String, value: String, color: Color, modifier: Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            Modifier
                .background(color.copy(0.2f))
                .padding(5.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(value, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Text(title)
        }
    }
}