package com.jminnovatech.mprint.ui.components

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Assignment
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jminnovatech.core.model.ComplaintListResponse
import com.jminnovatech.core.model.CompleteTaskRequest
import com.jminnovatech.core.model.ReachClientRequest
import com.jminnovatech.core.model.StartTaskRequest
import com.jminnovatech.core.model.TaskItem
import com.jminnovatech.core.utils.Resource
import com.jminnovatech.mprint.ui.screens.StatCard
import com.jminnovatech.mprint.viewmodel.MainViewModel
import com.jminnovatech.core.model.BackToBaseRequest
import com.jminnovatech.core.model.CancelTaskRequest
import java.io.File
import androidx.compose.ui.window.Dialog
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.HorizontalDivider
@RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
@Composable
fun AttendanceUI(
    vm: MainViewModel,
    complaintState: Resource<ComplaintListResponse>?,
    token: String,
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
    val context = LocalContext.current
    var selectedTask by remember {

        mutableStateOf<TaskItem?>(null)
    }


    var showTransportDialog by remember {

        mutableStateOf(false)
    }
    var showReachedDialog by remember {
        mutableStateOf(false)
    }
    var showCompleteDialog by remember {

        mutableStateOf(false)
    }
    var showBackDialog by remember {

        mutableStateOf(false)
    }
    var showCancelDialog by remember {
        mutableStateOf(false)
    }
    var showHistoryDialog by remember {
        mutableStateOf(false)
    }

    var showLeaveApprovalDialog by remember {
        mutableStateOf(false)
    }
    when (val reachState = vm.reachClientState) {

        is Resource.Loading -> {

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {

                CircularProgressIndicator()
            }
        }

        is Resource.Success -> {

            LaunchedEffect(reachState) {

                // 🔥 reload task list
                vm.loadComplaintList(token)

                // 🔥 close old dialog
                selectedTask = null

                // 🔥 clear state
                vm.clearReachState()
            }
        }

        is Resource.Error -> {

            LaunchedEffect(reachState) {

                vm.clearReachState()
            }
        }

        else -> {}
    }

    LaunchedEffect(vm.cancelTaskState) {

        val state = vm.cancelTaskState

        if (state is Resource.Success) {

            if (state.data?.status == true) {

                showCancelDialog = false

                selectedTask = null

                vm.loadComplaintList(token)
            }
        }
    }

    LaunchedEffect(vm.approveLeaveState){

        when(val state=vm.approveLeaveState){

            is Resource.Success->{

                Toast.makeText(

                    context,

                    "Leave ${state.data.msg}",

                    Toast.LENGTH_SHORT

                ).show()

                vm.loadManagerLeaves(token)

                vm.clearApproveState()

            }

            is Resource.Error->{

                Toast.makeText(

                    context,

                    state.message?:"Error",

                    Toast.LENGTH_SHORT

                ).show()

            }

            else->{}
        }

    }
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
        val pendingCount =

            (vm.managerLeaveState as? Resource.Success)

                ?.data

                ?.data

                ?.size ?: 0
        if (pendingCount > 0) {

            Spacer(Modifier.height(12.dp))

            ManagerApprovalCard(

                pendingCount = pendingCount,

                onClick = {

                    vm.loadManagerLeaves(token)

                    showLeaveApprovalDialog = true

                }

            )

            Spacer(Modifier.height(12.dp))
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




        Spacer(Modifier.height(12.dp))

//        when (val taskState = vm.complaintListState) {
//
//            is Resource.Loading -> {
//
//                CircularProgressIndicator()
//            }
//
//            is Resource.Success -> {
//
//                Column(
//                    verticalArrangement =
//                        Arrangement.spacedBy(14.dp)
//                ) {
//
//                    taskState.data.data.forEach { task ->
//
//                        TaskCard(
//
//                            task = task,
//
//                            onView = {
//
//                                selectedTask = task
//                            }
//                        )
//                    }
//                }
//
//            }
//
//            is Resource.Error -> {
//
//                Text(
//                    taskState.message ?: "Error"
//                )
//            }
//
//            else -> {}
//        }

        Spacer(Modifier.height(12.dp))

        when(val state = vm.complaintListState) {

            is Resource.Loading<*> -> {

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),

                    contentAlignment = Alignment.Center
                ) {

                    CircularProgressIndicator()
                }
            }


            is Resource.Success<*> -> {

                val data =
                    (state as Resource.Success<
                            ComplaintListResponse
                            >).data

                Column {

                    Row(

                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp),

                        verticalAlignment =
                            Alignment.CenterVertically
                    ) {

                        Text(

                            text = "Assigned Tasks",

                            style =
                                MaterialTheme.typography
                                    .headlineSmall,

                            fontWeight =
                                FontWeight.Bold
                        )

                        Spacer(
                            Modifier.weight(1f)
                        )

                        FilledTonalButton(

                            onClick = {

                                vm.loadHistory(token)

                                showHistoryDialog = true
                            },

                            shape =
                                RoundedCornerShape(12.dp),

                            contentPadding =
                                PaddingValues(

                                    horizontal = 12.dp,

                                    vertical = 4.dp
                                )
                        ) {

                            Text(
                                "History"
                            )
                        }

                        Spacer(
                            Modifier.width(8.dp)
                        )

                        Surface(

                            shape =
                                RoundedCornerShape(50),

                            color =
                                Color(0xFF2563EB)
                        ) {

                            Text(

                                text =
                                    "${data.data.size}",

                                color =
                                    Color.White,

                                modifier =
                                    Modifier.padding(
                                        horizontal = 12.dp,
                                        vertical = 4.dp
                                    )
                            )
                        }
                    }

                    if (
                        data.data.isEmpty()
                    ) {

                        EmptyTaskAnimation()

                    } else {

                        LazyColumn(

                            modifier =
                                Modifier.heightIn(
                                    max = 550.dp
                                ),

                            verticalArrangement =
                                Arrangement.spacedBy(
                                    12.dp
                                )
                        ) {

                            items(

                                data.data,

                                key = {

                                    it.sl
                                }

                            ) { task ->

                                TaskCard(

                                    task = task,

                                    onView = {

                                        selectedTask = task
                                    },

                                    onCancel = {

                                        selectedTask = task

                                        showCancelDialog = true
                                    }
                                )
                            }
                        }
                    }
                }
            }


            is Resource.Error<*> -> {

                Text(
                    state.message ?: "Error"
                )
            }

            else -> {}
        }

    }
    if (selectedTask != null) {

        selectedTask?.let { task ->

            TaskDetailsDialog(

                task = task,

                onDismiss = {

                    selectedTask = null
                },

                onStart = {

                    selectedTask = task

                    showTransportDialog = true
                },

                onReached = {
                    showReachedDialog = true
                },
                onCancel = {

                    showCancelDialog = true
                },
                onComplete = {

                    showCompleteDialog = true
                },
                onBackToBase = {

                    showBackDialog = true
                }
            )
        }
    }
    if (
        showTransportDialog &&
        selectedTask != null
    ) {

        TransportDialog(

            onDismiss = {

                showTransportDialog = false
            },

            onSubmit = { transport, odo ->

                showTransportDialog = false

                vm.startTask(

                    token = token,

                    body = StartTaskRequest(

                        task_id =
                            selectedTask!!.sl,

                        complain_id =
                            selectedTask!!.complain_id,

                        mode_of_transport =
                            transport,

                        start_odo = odo,

                        lat = lat,

                        long = long
                    )
                )
            }
        )
    }
    when (val startState = vm.startTaskState) {

        is Resource.Loading -> {

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {

                CircularProgressIndicator()
            }
        }

        is Resource.Success -> {

            LaunchedEffect(startState) {

                // 🔥 reload task list
                vm.loadComplaintList(token)

                // 🔥 close dialog
                showTransportDialog = false

                // 🔥 refresh selected task
                selectedTask = null

                // 🔥 clear state
                vm.clearStartTaskState()
            }
        }

        is Resource.Error -> {

            LaunchedEffect(startState) {

                vm.clearStartTaskState()
            }
        }

        else -> {}
    }
    if (
        showReachedDialog &&
        selectedTask != null
    ) {

        ReachedClientDialog(
            task = selectedTask!!,
            onDismiss = {

                showReachedDialog = false
            },

            onSubmit = { odo ->

                showReachedDialog = false

                vm.reachClient(

                    token,

                    ReachClientRequest(

                        task_id =
                            selectedTask!!.sl,

                        reach_odo = odo,

                        lat = lat,

                        long = long
                    )
                )
            }
        )

    }

    if (
        showCompleteDialog &&
        selectedTask != null
    ) {

        CompleteWorkDialog(

            onDismiss = {

                showCompleteDialog = false
            },

            onSubmit = {


                    status,

                    problem,

                    workDone,

                    remarks,

                    customerName,

                    customerPhone,

                    customerDesignation,

                    signature ->

                val file = File(signature)

                vm.uploadSignature(

                    token,

                    file

                ) { uploadedUrl ->

                    vm.completeTask(

                        token,

                        CompleteTaskRequest(

                            task_id =
                                selectedTask!!.sl,

                            complain_id =
                                selectedTask!!.complain_id,

                            work_status =
                                status,

                            remarks =
                                "$problem\n$workDone\n$remarks",

                            image_link = "",

                            signature_link =
                                uploadedUrl,

                            customer_name =
                                customerName,

                            customer_phone =
                                customerPhone,

                            customer_designation =
                                customerDesignation,

                            lat = lat,

                            long = long
                        )
                    )
                }
            }
        )
    }
    when(val state = vm.completeTaskState){

        is Resource.Loading -> {

            Loader()
        }

        is Resource.Success -> {

            LaunchedEffect(state){

                Toast.makeText(
                    context,
                    "Task Completed",
                    Toast.LENGTH_SHORT
                ).show()

                vm.loadComplaintList(token)

                showCompleteDialog = false

                selectedTask = null

                vm.clearCompleteTaskState()
            }
        }

        is Resource.Error -> {

            LaunchedEffect(state){

                Toast.makeText(
                    context,
                    state.message,
                    Toast.LENGTH_SHORT
                ).show()

                vm.clearCompleteTaskState()
            }
        }

        else -> {}
    }
    if (

        showBackDialog &&
        selectedTask != null

    ) {

        BackToBaseDialog(

            transport =
                selectedTask!!
                    .mode_of_transport
                    ?: "Bike",

            onDismiss = {

                showBackDialog = false
            },

            onSubmit = {

                    odo,
                    cost ->

                showBackDialog = false

                vm.backToBase(

                    token,

                    BackToBaseRequest(

                        task_id =
                            selectedTask!!.sl,

                        complain_id =
                            selectedTask!!
                                .complain_id,

                        end_odo = odo,

                        travel_cost = cost,

                        lat = lat,

                        long = long
                    )
                )
            }
        )
    }
    when (

        val state =
            vm.backToBaseState

    ) {

        is Resource.Success -> {

            LaunchedEffect(state) {

                vm.loadComplaintList(
                    token
                )

                selectedTask = null

                vm.clearBackState()

                Toast.makeText(

                    context,

                    "Returned To Base",

                    Toast.LENGTH_SHORT

                ).show()
            }
        }

        else -> {}
    }
    if (showCancelDialog) {

        CancelTaskDialog(

            transport =
                selectedTask?.mode_of_transport
                    ?: "",

            onDismiss = {

                showCancelDialog = false
            },

            onSubmit = {

                    reason,
                    odo,
                    cost ->

                vm.cancelTask(
                    token,

                    CancelTaskRequest(
                        task_id = selectedTask!!.sl,
                        complain_id = selectedTask!!.complain_id,
                        cancel_reason = reason,
                        end_odo = odo,
                        travel_cost = cost,
                        lat = lat,
                        long = long
                    )
                )
            }
        )
    }
    if (showHistoryDialog) {

        val historyState = vm.historyState

        Dialog(

            onDismissRequest = {

                showHistoryDialog = false
            }

        ) {

            Card(

                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.90f),

                shape = RoundedCornerShape(24.dp)

            ) {

                Column(

                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFFF8FAFC))
                ) {

                    // HEADER

                    Row(

                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFF2563EB))
                            .padding(18.dp),

                        horizontalArrangement =
                            Arrangement.SpaceBetween,

                        verticalAlignment =
                            Alignment.CenterVertically

                    ) {

                        Column {

                            Text(

                                "Task History",

                                color = Color.White,

                                fontSize = 20.sp,

                                fontWeight = FontWeight.Bold
                            )

                            Text(

                                "Last 30 Days",

                                color = Color.White.copy(.8f)
                            )
                        }

                        FilledIconButton(

                            onClick = {

                                showHistoryDialog = false
                            },

                            colors =
                                IconButtonDefaults.filledIconButtonColors(

                                    containerColor =
                                        Color.White
                                )
                        ) {

                            Icon(

                                Icons.Default.Close,

                                contentDescription = null,

                                tint = Color.Black
                            )
                        }
                    }

                    when (val state = historyState) {

                        is Resource.Loading -> {

                            Box(

                                modifier = Modifier
                                    .fillMaxSize(),

                                contentAlignment =
                                    Alignment.Center

                            ) {

                                CircularProgressIndicator()
                            }
                        }

                        is Resource.Success -> {

                            val data =
                                state.data.data

                            if (data.isEmpty()) {

                                Box(

                                    modifier =
                                        Modifier.fillMaxSize(),

                                    contentAlignment =
                                        Alignment.Center

                                ) {

                                    Text(

                                        "No History Found",

                                        fontSize = 18.sp,

                                        fontWeight =
                                            FontWeight.SemiBold
                                    )
                                }

                            } else {

                                LazyColumn(

                                    modifier = Modifier
                                        .fillMaxSize(),

                                    contentPadding =
                                        PaddingValues(14.dp),

                                    verticalArrangement =
                                        Arrangement.spacedBy(10.dp)

                                ) {

                                    items(

                                        items = data,

                                        key = { it.sl }

                                    ) { task ->

                                        ElevatedCard(

                                            modifier = Modifier
                                                .fillMaxWidth(),

                                            shape =
                                                RoundedCornerShape(18.dp),

                                            elevation =
                                                CardDefaults.elevatedCardElevation(
                                                    defaultElevation = 4.dp
                                                )
                                        ) {

                                            Column(

                                                modifier =
                                                    Modifier.padding(16.dp)
                                            ) {

                                                Row(

                                                    modifier =
                                                        Modifier.fillMaxWidth(),

                                                    horizontalArrangement =
                                                        Arrangement.SpaceBetween,

                                                    verticalAlignment =
                                                        Alignment.CenterVertically
                                                ) {

                                                    Text(

                                                        task.complain_id,

                                                        fontWeight =
                                                            FontWeight.Bold,

                                                        fontSize = 15.sp
                                                    )

                                                    SuggestionChip(

                                                        onClick = {},

                                                        enabled = false,

                                                        label = {

                                                            Text(

                                                                task.task_status2
                                                                    ?: "-"
                                                            )
                                                        }
                                                    )
                                                }

                                                Spacer(
                                                    Modifier.height(12.dp)
                                                )

                                                Text(

                                                    task.client_name ?: "",

                                                    fontWeight =
                                                        FontWeight.SemiBold,

                                                    fontSize = 16.sp
                                                )

                                                Spacer(
                                                    Modifier.height(4.dp)
                                                )

                                                Text(
                                                    "Model : ${task.model ?: "-"}"
                                                )

                                                Text(
                                                    "Engineer : ${task.assigned_name ?: "-"}"
                                                )

                                                Text(
                                                    "Status : ${task.task_status ?: "-"}"
                                                )

                                                Text(
                                                    "Workflow : ${task.task_status2 ?: "-"}"
                                                )

                                                Spacer(
                                                    Modifier.height(8.dp)
                                                )

                                                HorizontalDivider()

                                                Spacer(
                                                    Modifier.height(8.dp)
                                                )

                                                Text(

                                                    task.complain_datetime ?: "",

                                                    color = Color.Gray,

                                                    fontSize = 12.sp
                                                )
                                            }
                                        }
                                    }

                                    item {

                                        Spacer(
                                            Modifier.height(20.dp)
                                        )
                                    }
                                }
                            }
                        }

                        is Resource.Error -> {

                            Box(

                                modifier =
                                    Modifier.fillMaxSize(),

                                contentAlignment =
                                    Alignment.Center

                            ) {

                                Text(

                                    state.message ?: "Something went wrong",

                                    color = Color.Red
                                )
                            }
                        }

                        else -> {

                            Box(

                                modifier =
                                    Modifier.fillMaxSize(),

                                contentAlignment =
                                    Alignment.Center

                            ) {

                                Text("Loading...")
                            }
                        }
                    }
                }
            }
        }
    }

    if(showLeaveApprovalDialog){

        LeaveApprovalDialog(

            state=

                vm.managerLeaveState,

            onClose={

                showLeaveApprovalDialog=false

            },

            onApprove={

                vm.approveLeave(

                    token,

                    it,

                    "APPROVE"

                )

            },

            onReject={

                vm.approveLeave(

                    token,

                    it,

                    "REJECT"

                )

            }

        )

    }

}


@Composable
fun EmptyTaskAnimation() {

    Column(

        modifier = Modifier
            .fillMaxWidth()
            .padding(30.dp),

        horizontalAlignment =
            Alignment.CenterHorizontally
    ) {

        Icon(

            Icons.AutoMirrored.Filled.Assignment,

            contentDescription = null,

            modifier =
                Modifier.size(80.dp),

            tint =
                Color.LightGray
        )

        Spacer(
            Modifier.height(12.dp)
        )

        Text(

            "No Assigned Tasks",

            color =
                Color.Gray
        )
    }
}