package com.jminnovatech.mprint.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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


}

@Composable
fun AttendanceUI(
    name: String,
    designation: String,
    empId: String,
    present: String,
    leave: String,
    lat: String,
    long: String,
    distance: String,
    inTime: String?,
    outTime: String?,
    workingTime: String,
    isCheckedIn: Boolean,
    isCheckedOut: Boolean,
    isActionLoading: Boolean,
    isGpsEnabled: Boolean,
    isLocationLoading: Boolean,
    onCheckIn: () -> Unit,
    onCheckOut: () -> Unit,
    onLeaveClick: () -> Unit
    ) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(5.dp)
    ) {

        // 🔵 PROFILE CARD
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.Transparent
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .background(
                        Brush.horizontalGradient(
                            listOf(Color(0xFF1976D2), Color(0xFF26C6DA))
                        )
                    )
                    .padding(5.dp)
            ) {

                Row(verticalAlignment = Alignment.CenterVertically) {

                    Icon(
                        Icons.Default.Person,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(40.dp)
                    )

                    Spacer(Modifier.width(5.dp))

                    Column {
                        Text(name, color = Color.White, fontSize = 18.sp)
                        Text(designation, color = Color.White.copy(0.8f))
                        Text("ID: $empId", color = Color.White.copy(0.8f))
                    }

                    Spacer(Modifier.weight(1f))

                    Icon(
                        Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = Color.White
                    )
                }
            }
        }

        Spacer(Modifier.height(5.dp))

        // 🟢 PRESENT / 🔴 LEAVE
        Row(horizontalArrangement = Arrangement.spacedBy(5.dp)) {

            StatCard("Present", present, Color(0xFF00C853), Modifier.weight(1f))
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clickable {
                        onLeaveClick()
                    }
            ) {
                StatCard(
                    "Leave",
                    leave,
                    Color(0xFFD50000),
                    Modifier.fillMaxWidth()
                )
            }
        }

        Spacer(Modifier.height(12.dp))

        // 📍 LOCATION CARD
        Card(
            shape = RoundedCornerShape(16.dp),

            colors = CardDefaults.cardColors(
                Color(0xFF0D1B2A)
            ),

            modifier = Modifier.fillMaxWidth()
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 14.dp,
                        vertical = 12.dp
                    ),

                verticalAlignment = Alignment.CenterVertically,

                horizontalArrangement =
                    Arrangement.SpaceBetween
            ) {

                // 📍 LEFT SIDE
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Icon(
                        Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = Color.White
                    )

                    Spacer(Modifier.width(8.dp))

                    if (isLocationLoading) {

                        Row(
                            verticalAlignment =
                                Alignment.CenterVertically
                        ) {

                            CircularProgressIndicator(
                                modifier = Modifier.size(16.dp),
                                strokeWidth = 2.dp,
                                color = Color.White
                            )

                            Spacer(Modifier.width(8.dp))

                            Text(
                                "Fetching...",
                                color = Color.White,
                                fontSize = 12.sp
                            )
                        }

                    } else {

                        Column {

                            Text(
                                "$lat , $long",
                                color = Color(0xFF00E676),
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )

                            Text(
                                distance,
                                color = Color.White.copy(0.7f),
                                fontSize = 11.sp
                            )
                        }
                    }
                }

                // 🔥 RIGHT SIDE GPS STATUS
                Surface(
                    shape = RoundedCornerShape(50),

                    color =
                        if (isGpsEnabled)
                            Color(0xFF00C853).copy(0.2f)
                        else
                            Color.Red.copy(0.2f)
                ) {

                    Text(
                        if (isGpsEnabled)
                            "GPS ON"
                        else
                            "GPS OFF",

                        color =
                            if (isGpsEnabled)
                                Color(0xFF00E676)
                            else
                                Color.Red,

                        modifier = Modifier.padding(
                            horizontal = 10.dp,
                            vertical = 5.dp
                        ),

                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp
                    )
                }
            }
        }

        Spacer(Modifier.height(14.dp))

        // ⏱ WORKING TIME CENTER
        Card(
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(6.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text("Total Working Time")

                Spacer(Modifier.height(10.dp))

                Text(
                    workingTime,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF00C853)
                )
            }
        }

        Spacer(Modifier.height(5.dp))

        // 🔘 IN / OUT BUTTONS
        Row(horizontalArrangement = Arrangement.spacedBy(5.dp)) {

            // IN BUTTON
            Button(
                onClick = {



                    onCheckIn()
                },
                enabled =
                    !isCheckedIn &&
                            !isActionLoading &&
                            !isLocationLoading &&
                            lat != "0.0" &&
                            long != "0.0",
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(30.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isCheckedIn) Color.LightGray else Color(0xFF22C55E)
                )
            ) {

                if (isActionLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(18.dp),
                        strokeWidth = 2.dp,
                        color = Color.White
                    )
                } else {
                    Text(if (isCheckedIn) "✔ IN" else "IN")
                }
            }

            // OUT BUTTON
            Button(
                onClick = {

                    onCheckOut()
                },
                enabled =
                    isCheckedIn &&
                            !isCheckedOut &&
                            !isActionLoading &&
                            !isLocationLoading &&
                            lat != "0.0" &&
                            long != "0.0",
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(30.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isCheckedOut) Color.LightGray else Color(0xFF2563EB)
                )
            ) {

                if (isActionLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(18.dp),
                        strokeWidth = 2.dp,
                        color = Color.White
                    )
                } else {
                    Text(if (isCheckedOut) "✔ OUT" else "OUT")
                }
            }}
        Spacer(Modifier.height(16.dp))

        // 🕒 IN OUT TIME
        Card(
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Column {
                    Text("Attendance In", color = Color(0xFF00C853))
                    Text(inTime ?: "--")
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text("Attendance Out", color = Color.Red)
                    Text(outTime ?: "--")
                }
            }
        }


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