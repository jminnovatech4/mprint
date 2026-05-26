package com.jminnovatech.mprint.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.jminnovatech.core.model.TaskItem

@Composable
fun TaskDetailsDialog(

    task: TaskItem,

    onDismiss: () -> Unit,

    onStart: () -> Unit,

    onReached: () -> Unit
) {

    val currentStep = remember(task) {

        when {

            task.back_to_base != null -> 5

            task.complete_work != null -> 4

            task.reach_client != null -> 3

            task.start_base != null -> 2

            else -> 1
        }
    }

    Dialog(
        onDismissRequest = onDismiss
    ) {

        Card(

            modifier = Modifier
                .fillMaxWidth(),

            shape = RoundedCornerShape(28.dp),

            colors = CardDefaults.cardColors(
                containerColor = Color.White
            )
        ) {

            Column(

                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(
                        rememberScrollState()
                    )
                    .padding(18.dp)
            ) {

                // HEADER
                Row(

                    modifier = Modifier
                        .fillMaxWidth(),

                    horizontalArrangement =
                        Arrangement.SpaceBetween,

                    verticalAlignment =
                        Alignment.CenterVertically
                ) {

                    Column {

                        Text(

                            task.complain_id,

                            fontSize = 24.sp,

                            fontWeight = FontWeight.Bold
                        )

                        Spacer(
                            Modifier.height(4.dp)
                        )

                        Text(

                            "Field Service Workflow",

                            color = Color.Gray
                        )
                    }

                    IconButton(
                        onClick = onDismiss
                    ) {

                        Icon(
                            Icons.Default.Close,
                            contentDescription = null
                        )
                    }
                }

                Spacer(Modifier.height(20.dp))

                // CLIENT DETAILS CARD
                Card(

                    shape = RoundedCornerShape(24.dp),

                    colors = CardDefaults.cardColors(
                        containerColor =
                            Color(0xFFF8FAFC)
                    )
                ) {

                    Column(
                        modifier = Modifier
                            .padding(18.dp)
                    ) {

                        TaskInfoRow(

                            icon = Icons.Default.Person,

                            title = "Client",

                            value =
                                task.client_name ?: "--"
                        )

                        TaskInfoRow(

                            icon = Icons.Default.Build,

                            title = "Model",

                            value =
                                task.model ?: "--"
                        )

                        TaskInfoRow(

                            icon = Icons.Default.Call,

                            title = "Mobile",

                            value =
                                task.mobile_no ?: "--"
                        )

                        TaskInfoRow(

                            icon = Icons.Default.Badge,

                            title = "Engineer",

                            value =
                                task.assigned_name ?: "--"
                        )

                        TaskInfoRow(

                            icon = Icons.Default.Work,

                            title = "Designation",

                            value =
                                task.designation ?: "--"
                        )

                        TaskInfoRow(

                            icon =
                                Icons.Default.CalendarMonth,

                            title = "Assigned Time",

                            value =
                                task.web_assigne_time ?: "--"
                        )
                    }
                }


                Spacer(Modifier.height(20.dp))

                Text(

                    "Workflow Timeline",

                    fontSize = 20.sp,

                    fontWeight = FontWeight.Bold
                )

                Spacer(Modifier.height(16.dp))

// ======================
// ASSIGNED
// ======================

                WorkflowStepCard(

                    title = "Assigned",

                    subtitle = "Task Assigned By Admin",

                    active = true,

                    completed = true,

                    color = Color(0xFFDCE7FF)
                )

                Spacer(Modifier.height(14.dp))




                // STEP 2
                WorkflowCard(

                    title = "Start Journey",

                    subtitle =
                        if (task.start_base == null)
                            "Waiting To Start"
                        else
                            "Journey Started",

                    completed =
                        currentStep >= 2,

                    active =
                        currentStep == 1,

                    expanded =
                        currentStep >= 2,

                    color = Color(0xFFEAF8EE)
                ) {

                    Column {

                        TaskValueRow(
                            "Transport",
                            task.mode_of_transport ?: "--"
                        )

                        TaskValueRow(
                            "Start ODO",
                            task.start_odo ?: "--"
                        )

                        TaskValueRow(
                            "Started At",
                            task.start_base ?: "--"
                        )

                        TaskValueRow(
                            "Latitude",
                            task.start_lat ?: "--"
                        )

                        TaskValueRow(
                            "Longitude",
                            task.start_long ?: "--"
                        )
                    }
                }

                Spacer(Modifier.height(14.dp))

                // STEP 3
                WorkflowCard(

                    title = "Reached Client",

                    subtitle =
                        if (task.reach_client == null)
                            "Pending"
                        else
                            "Client Reached",

                    completed =
                        currentStep >= 3,

                    active =
                        currentStep == 2,

                    expanded =
                        currentStep >= 3,

                    color = Color(0xFFFFF4E5)
                ) {

                    Column {

                        TaskValueRow(
                            "Reached ODO",
                            task.reach_odo ?: "--"
                        )

                        TaskValueRow(
                            "Reached Time",
                            task.reach_client ?: "--"
                        )

                        TaskValueRow(
                            "Latitude",
                            task.reach_lat ?: "--"
                        )

                        TaskValueRow(
                            "Longitude",
                            task.reach_long ?: "--"
                        )
                    }
                }

                Spacer(Modifier.height(14.dp))

                // STEP 4
                WorkflowCard(

                    title = "Complete Work",

                    subtitle =
                        if (task.complete_work == null)
                            "Pending"
                        else
                            "Work Completed",

                    completed =
                        currentStep >= 4,

                    active =
                        currentStep == 3,

                    expanded =
                        currentStep >= 4,

                    color = Color(0xFFF3E8FF)
                ) {

                    Column {

                        TaskValueRow(
                            "Remarks",
                            task.remarks ?: "--"
                        )

                        TaskValueRow(
                            "Completed At",
                            task.complete_work ?: "--"
                        )
                    }
                }

                Spacer(Modifier.height(14.dp))

                // STEP 5
                WorkflowCard(

                    title = "Back To Base",

                    subtitle =
                        if (task.back_to_base == null)
                            "Pending"
                        else
                            "Returned To Base",

                    completed =
                        currentStep >= 5,

                    active =
                        currentStep == 4,

                    expanded =
                        currentStep >= 5,

                    color = Color(0xFFFFE8E8)
                ) {

                    Column {

                        TaskValueRow(
                            "End ODO",
                            task.back_to_base_odo ?: "--"
                        )

                        TaskValueRow(
                            "Travel Cost",
                            task.traveling_cost ?: "--"
                        )

                        TaskValueRow(
                            "Returned At",
                            task.back_to_base ?: "--"
                        )
                    }
                }

                Spacer(Modifier.height(24.dp))

                // ACTION BUTTONS
                when {

                    // START
                    task.start_base == null -> {

                        GradientButton(

                            text = "START TASK",

                            icon =
                                Icons.Default.PlayArrow,

                            color1 =
                                Color(0xFF16A34A),

                            color2 =
                                Color(0xFF22C55E),

                            onClick = onStart
                        )
                    }

                    // REACHED
                    task.start_base != null &&
                            task.reach_client == null -> {

                        GradientButton(

                            text = "REACHED CLIENT",

                            icon =
                                Icons.Default.LocationOn,

                            color1 =
                                Color(0xFFFF9800),

                            color2 =
                                Color(0xFFFFB74D),

                            onClick = onReached
                        )
                    }
                }

                Spacer(Modifier.height(14.dp))

                OutlinedButton(

                    onClick = onDismiss,

                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),

                    shape = RoundedCornerShape(18.dp)
                ) {

                    Text("CLOSE")
                }
            }
        }
    }
}

@Composable
fun WorkflowCard(

    title: String,

    subtitle: String,

    completed: Boolean,

    active: Boolean,

    expanded: Boolean,

    color: Color,

    content: @Composable () -> Unit
) {

    Card(

        shape = RoundedCornerShape(24.dp),

        colors = CardDefaults.cardColors(
            containerColor = color
        )
    ) {

        Column(
            modifier = Modifier.padding(18.dp)
        ) {

            Row(
                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                Box(

                    modifier = Modifier

                        .size(44.dp)

                        .background(

                            if (completed)
                                Color(0xFF2563EB)
                            else
                                Color.LightGray,

                            CircleShape
                        ),

                    contentAlignment =
                        Alignment.Center
                ) {

                    Icon(

                        if (completed)
                            Icons.Default.Check
                        else
                            Icons.Default.RadioButtonUnchecked,

                        contentDescription = null,

                        tint = Color.White
                    )
                }

                Spacer(Modifier.width(14.dp))

                Column {

                    Text(

                        title,

                        fontWeight = FontWeight.Bold,

                        fontSize = 17.sp
                    )

                    Text(
                        subtitle,
                        color = Color.DarkGray
                    )
                }
            }

            AnimatedVisibility(expanded) {

                Column {

                    Spacer(Modifier.height(18.dp))

                    content()
                }
            }
        }
    }
}

@Composable
fun GradientButton(

    text: String,

    icon: androidx.compose.ui.graphics.vector.ImageVector,

    color1: Color,

    color2: Color,

    onClick: () -> Unit
) {

    Button(

        onClick = onClick,

        modifier = Modifier
            .fillMaxWidth()
            .height(58.dp),

        shape = RoundedCornerShape(20.dp),

        contentPadding = PaddingValues(),

        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent
        )
    ) {

        Box(

            modifier = Modifier

                .fillMaxSize()

                .background(

                    Brush.horizontalGradient(
                        listOf(color1, color2)
                    )
                ),

            contentAlignment = Alignment.Center
        ) {

            Row(
                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                Icon(
                    icon,
                    contentDescription = null,
                    tint = Color.White
                )

                Spacer(Modifier.width(8.dp))

                Text(
                    text,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun TaskInfoRow(

    icon: androidx.compose.ui.graphics.vector.ImageVector,

    title: String,

    value: String
) {

    Row(

        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),

        verticalAlignment =
            Alignment.CenterVertically
    ) {

        Icon(
            icon,
            contentDescription = null,
            tint = Color(0xFF2563EB)
        )

        Spacer(Modifier.width(14.dp))

        Column {

            Text(
                title,
                color = Color.Gray,
                fontSize = 13.sp
            )

            Text(
                value,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun TaskValueRow(

    title: String,

    value: String
) {

    Row(

        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),

        horizontalArrangement =
            Arrangement.SpaceBetween
    ) {

        Text(
            title,
            color = Color.DarkGray
        )

        Text(
            value,
            fontWeight = FontWeight.Bold
        )
    }
}