package com.jminnovatech.mprint.ui.screens

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Pending
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import com.google.accompanist.swiperefresh.*
import com.jminnovatech.core.datastore.SessionManager
import com.jminnovatech.core.model.ApplyLeaveRequest
import com.jminnovatech.core.utils.Resource
import com.jminnovatech.mprint.viewmodel.MainViewModel
import kotlinx.coroutines.delay
import com.maxkeppeker.sheets.core.models.base.rememberUseCaseState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarSelection

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LeaveScreen(vm: MainViewModel) {
    val fromDateState = rememberUseCaseState()
    val toDateState = rememberUseCaseState()
    val context = LocalContext.current
    val session = SessionManager(context)

    val token = session.getToken() ?: ""

    val leaveState = vm.leaveTypesState
    val applyState = vm.applyLeaveState
    val historyState = vm.myLeavesState

    var selectedType by remember { mutableStateOf("") }
    var selectedTypeName by remember { mutableStateOf("") }

    var reason by remember { mutableStateOf("") }

    var fromDate by remember { mutableStateOf("") }
    var toDate by remember { mutableStateOf("") }

    var isRefreshing by remember {
        mutableStateOf(false)
    }

    val refreshState = rememberSwipeRefreshState(
        isRefreshing
    )

    LaunchedEffect(Unit) {

        vm.loadLeaveTypes(token)
        vm.loadMyLeaves(token)
    }

    val infinite = rememberInfiniteTransition(
        label = ""
    )

    val animatedScale by infinite.animateFloat(
        initialValue = 0.95f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            tween(1200),
            RepeatMode.Reverse
        ),
        label = ""
    )
    var apiFromDate by remember {
        mutableStateOf("")
    }

    var apiToDate by remember {
        mutableStateOf("")
    }
    var selectedAvailable by remember {
        mutableStateOf(0)
    }

    var totalDays by remember {
        mutableLongStateOf(0L)
    }

    var showConfirm by remember {
        mutableStateOf(false)
    }
    SwipeRefresh(
        state = refreshState,
        onRefresh = {

            isRefreshing = true

            vm.loadLeaveTypes(token)
            vm.loadMyLeaves(token)

            isRefreshing = false
        }
    ) {

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Color(0xFFF5F7FB)
                )
        ) {

            item {

                // 🔥 HEADER

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.horizontalGradient(
                                listOf(
                                    Color(0xFF1565C0),
                                    Color(0xFF42A5F5)
                                )
                            )
                        )
                        .padding(20.dp)
                ) {

                    Column {

                        Text(
                            "Leave Management",
                            color = Color.White,
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(Modifier.height(6.dp))

                        Text(
                            "Professional HRMS Leave System",
                            color = Color.White.copy(0.9f)
                        )
                    }
                }
            }

            item {

                Spacer(Modifier.height(14.dp))

                Text(
                    "Available Leaves",
                    modifier = Modifier.padding(horizontal = 16.dp),
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )

                Spacer(Modifier.height(10.dp))
            }

            item {

                when (leaveState) {

                    is Resource.Loading -> {

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(30.dp),
                            contentAlignment = Alignment.Center
                        ) {

                            CircularProgressIndicator()
                        }
                    }

                    is Resource.Success -> {

                        Row(
                            modifier = Modifier
                                .horizontalScroll(
                                    rememberScrollState()
                                )
                                .padding(horizontal = 16.dp)
                        ) {

                            leaveState.data.data.forEach { leave ->

                                Card(
                                    modifier = Modifier
                                        .padding(end = 14.dp)
                                        .width(220.dp)
                                        .graphicsLayer {

                                            scaleX = animatedScale
                                            scaleY = animatedScale
                                        }
                                        .clickable {

                                            selectedType = leave.id
                                            selectedTypeName = leave.name

                                            selectedAvailable = leave.available
                                        },

                                    colors = CardDefaults.cardColors(
                                        containerColor = Color.White
                                    ),

                                    shape = RoundedCornerShape(24.dp),

                                    elevation = CardDefaults.cardElevation(
                                        defaultElevation = 8.dp
                                    )
                                ) {

                                    Column(
                                        modifier = Modifier.padding(18.dp)
                                    ) {

                                        Text(
                                            leave.name,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 20.sp
                                        )

                                        Spacer(Modifier.height(12.dp))

                                        Text(
                                            "${leave.available} Available",
                                            color = Color(0xFF1565C0),
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 24.sp
                                        )

                                        Spacer(Modifier.height(10.dp))

                                        LinearProgressIndicator(
                                            progress = {
                                                leave.available.toFloat() /
                                                        leave.total.toFloat()
                                            },
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(10.dp)
                                                .clip(
                                                    RoundedCornerShape(50)
                                                )
                                        )

                                        Spacer(Modifier.height(10.dp))

                                        Text(
                                            "Used ${leave.used} / ${leave.total}",
                                            color = Color.Gray
                                        )
                                    }
                                }
                            }
                        }
                    }

                    else -> {}
                }
            }

            // 🔥 APPLY FORM

            item {

                AnimatedVisibility(
                    visible = selectedType.isNotEmpty(),
                    enter = fadeIn() + slideInVertically()
                ) {

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),

                        shape = RoundedCornerShape(24.dp)
                    ) {

                        Column(
                            modifier = Modifier.padding(20.dp)
                        ) {

                            Text(
                                "Apply $selectedTypeName",
                                fontWeight = FontWeight.Bold,
                                fontSize = 22.sp
                            )

                            Spacer(Modifier.height(20.dp))

                            OutlinedCard(

                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        fromDateState.show()
                                    }

                            ) {

                                Row(
                                    modifier = Modifier.padding(16.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {

                                    Icon(
                                        Icons.Default.CalendarMonth,
                                        null
                                    )

                                    Spacer(Modifier.width(10.dp))

                                    Text(
                                        if (apiFromDate.isEmpty())
                                            "Select From Date"
                                        else
                                            apiFromDate
                                    )
                                }
                            }





                            Spacer(Modifier.height(14.dp))

                            OutlinedCard(

                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        toDateState.show()
                                    }

                            ) {

                                Row(
                                    modifier = Modifier.padding(16.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {

                                    Icon(
                                        Icons.Default.DateRange,
                                        null
                                    )

                                    Spacer(Modifier.width(10.dp))

                                    Text(
                                        if (apiToDate.isEmpty())
                                            "Select To Date"
                                        else
                                            apiToDate
                                    )
                                }
                            }

                            Spacer(Modifier.height(24.dp))
                            OutlinedTextField(
                                value = reason,
                                onValueChange = {
                                    reason = it
                                },

                                modifier = Modifier.fillMaxWidth(),

                                label = {
                                    Text("Reason")
                                },

                                placeholder = {
                                    Text("Enter leave reason")
                                },

                                leadingIcon = {
                                    Icon(
                                        Icons.Default.Edit,
                                        contentDescription = null
                                    )
                                },

                                shape = RoundedCornerShape(18.dp),

                                minLines = 3
                            )
                            Spacer(Modifier.height(12.dp))

                            Text(
                                "Selected Days: $totalDays",
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF1565C0)
                            )

                            Spacer(Modifier.height(4.dp))

                            Text(
                                "Available Balance: $selectedAvailable",
                                color = Color.Gray
                            )
                            Spacer(Modifier.height(24.dp))

                            Button(
                                onClick = {

                                    if (apiFromDate.isEmpty()) {

                                        Toast.makeText(
                                            context,
                                            "Select From Date",
                                            Toast.LENGTH_SHORT
                                        ).show()

                                        return@Button
                                    }

                                    if (apiToDate.isEmpty()) {

                                        Toast.makeText(
                                            context,
                                            "Select To Date",
                                            Toast.LENGTH_SHORT
                                        ).show()

                                        return@Button
                                    }

                                    if (reason.trim().isEmpty()) {

                                        Toast.makeText(
                                            context,
                                            "Enter Reason",
                                            Toast.LENGTH_SHORT
                                        ).show()

                                        return@Button
                                    }

                                    if (totalDays <= 0) {

                                        Toast.makeText(
                                            context,
                                            "Invalid leave days",
                                            Toast.LENGTH_SHORT
                                        ).show()

                                        return@Button
                                    }

                                    if (totalDays > selectedAvailable) {

                                        Toast.makeText(
                                            context,
                                            "Leave balance exceeded",
                                            Toast.LENGTH_SHORT
                                        ).show()

                                        return@Button
                                    }

                                    showConfirm = true
                                },

                                enabled =
                                    totalDays > 0 &&
                                            totalDays <= selectedAvailable,

                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(56.dp),

                                shape = RoundedCornerShape(18.dp)
                            ) {

                                if (applyState is Resource.Loading) {

                                    CircularProgressIndicator(
                                        modifier = Modifier.size(20.dp),
                                        color = Color.White,
                                        strokeWidth = 2.dp
                                    )

                                } else {

                                    Text(
                                        "Submit Leave",
                                        fontSize = 18.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // 🔥 HISTORY

            item {

                Spacer(Modifier.height(12.dp))

                Text(
                    "Leave History",
                    modifier = Modifier.padding(horizontal = 16.dp),
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            }

            when(historyState) {

                is Resource.Success -> {

                    items(historyState.data.data) { item ->

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    horizontal = 16.dp,
                                    vertical = 8.dp
                                ),

                            shape = RoundedCornerShape(22.dp)
                        ) {

                            Column(
                                modifier = Modifier.padding(18.dp)
                            ) {

                                Row(
                                    horizontalArrangement =
                                        Arrangement.SpaceBetween,

                                    modifier = Modifier.fillMaxWidth()
                                ) {

                                    Text(
                                        item.leave_type,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 20.sp
                                    )

                                    StatusChip(item.status)
                                }

                                Spacer(Modifier.height(10.dp))

                                Text(
                                    "${
                                        item.from_date.take(10)
                                    } → ${
                                        item.to_date.take(10)
                                    }"
                                )

                                Spacer(Modifier.height(4.dp))

                                Text(
                                    "${item.days} Days"
                                )

                                Spacer(Modifier.height(6.dp))

                                Text(
                                    item.reason ?: ""
                                )
                            }
                        }
                    }
                }

                else -> {}
            }

            item {
                Spacer(Modifier.height(100.dp))
            }
        }
    }

    // 🔥 APPLY RESPONSE
    if (showConfirm) {

        AlertDialog(

            onDismissRequest = {
                showConfirm = false
            },

            title = {
                Text("Confirm Leave")
            },

            text = {

                Column {

                    Text("Type: $selectedTypeName")

                    Spacer(Modifier.height(6.dp))

                    Text("Days: $totalDays")

                    Spacer(Modifier.height(6.dp))

                    Text("From: $fromDate")

                    Spacer(Modifier.height(6.dp))

                    Text("To: $toDate")
                }
            },

            confirmButton = {

                Button(

                    onClick = {

                        showConfirm = false

                        vm.applyLeave(
                            token,
                            ApplyLeaveRequest(
                                selectedType,
                                apiFromDate,
                                apiToDate,
                                reason
                            )
                        )
                    }

                ) {

                    Text("Confirm")
                }
            },

            dismissButton = {

                TextButton(
                    onClick = {
                        showConfirm = false
                    }
                ) {

                    Text("Cancel")
                }
            }
        )
    }
    when (applyState) {

        is Resource.Success -> {

            LaunchedEffect(Unit) {

                Toast.makeText(
                    context,
                    applyState.data.msg,
                    Toast.LENGTH_SHORT
                ).show()

                selectedType = ""
                selectedTypeName = ""

                selectedAvailable = 0

                reason = ""

                fromDate = ""
                toDate = ""

                apiFromDate = ""
                apiToDate = ""

                totalDays = 0

                vm.clearLeaveState()

                vm.loadLeaveTypes(token)
                vm.loadMyLeaves(token)
            }
        }

        is Resource.Error -> {

            LaunchedEffect(Unit) {

                Toast.makeText(
                    context,
                    applyState.message,
                    Toast.LENGTH_SHORT
                ).show()

                vm.clearLeaveState()
            }
        }

        else -> {}
    }
    CalendarDialog(
        state = fromDateState,

        selection = CalendarSelection.Date { date ->

            apiFromDate = date.toString()

            fromDate =
                "${date.dayOfMonth.toString().padStart(2,'0')}/" +
                        "${date.monthValue.toString().padStart(2,'0')}/" +
                        date.year

            if (
                apiFromDate.isNotEmpty() &&
                apiToDate.isNotEmpty()
            ) {

                val start =
                    java.time.LocalDate.parse(apiFromDate)

                val end =
                    java.time.LocalDate.parse(apiToDate)

                totalDays =
                    java.time.temporal.ChronoUnit
                        .DAYS
                        .between(start, end) + 1
            }
        }
    )
    CalendarDialog(
        state = toDateState,

        selection = CalendarSelection.Date { date ->

            apiToDate = date.toString()

            toDate =
                "${date.dayOfMonth.toString().padStart(2,'0')}/" +
                        "${date.monthValue.toString().padStart(2,'0')}/" +
                        date.year

            if (
                apiFromDate.isNotEmpty() &&
                apiToDate.isNotEmpty()
            ) {

                val start =
                    java.time.LocalDate.parse(apiFromDate)

                val end =
                    java.time.LocalDate.parse(apiToDate)

                totalDays =
                    java.time.temporal.ChronoUnit
                        .DAYS
                        .between(start, end) + 1
            }
        }
    )
}

@Composable
fun StatusChip(status: String) {

    val color = when(status.uppercase()) {

        "APPROVED" -> Color(0xFF00C853)

        "REJECTED" -> Color.Red

        else -> Color(0xFFFF9800)
    }

    Surface(
        color = color.copy(0.15f),
        shape = RoundedCornerShape(50)
    ) {

        Text(
            status,
            color = color,
            modifier = Modifier.padding(
                horizontal = 14.dp,
                vertical = 6.dp
            ),
            fontWeight = FontWeight.Bold
        )
    }
}