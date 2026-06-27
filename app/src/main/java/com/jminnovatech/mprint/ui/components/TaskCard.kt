package com.jminnovatech.mprint.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jminnovatech.core.model.TaskItem

@Composable
fun TaskCard(

    task: TaskItem,

    onView: () -> Unit,
    onCancel: () -> Unit
) {

    Card(

        modifier = Modifier.fillMaxWidth(),

        shape = RoundedCornerShape(20.dp),

        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),

        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        )
    ) {

        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            Row(

                modifier = Modifier.fillMaxWidth(),

                horizontalArrangement =
                    Arrangement.SpaceBetween,

                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                Column {

                    Text(

                        task.complain_id,

                        fontWeight = FontWeight.Bold,

                        fontSize = 20.sp
                    )

                    Spacer(Modifier.height(4.dp))

                    Text(
                        task.client_name ?: ""
                    )

                    Text(
                        task.model ?: ""
                    )
                }

                Surface(

                    color = Color(0xFFFF9800),

                    shape =
                        RoundedCornerShape(50)
                ) {

                    Text(

                        task.task_status ?: "Assigned",

                        color = Color.White,

                        modifier = Modifier.padding(
                            horizontal = 12.dp,
                            vertical = 6.dp
                        )
                    )
                }
            }

            Spacer(Modifier.height(14.dp))

            Row(

                modifier =
                    Modifier.fillMaxWidth(),

                horizontalArrangement =
                    Arrangement.spacedBy(8.dp)
            ) {

                Button(

                    onClick = {

                        onView()
                    },

                    modifier =
                        Modifier.weight(0.7f),

                    shape =
                        RoundedCornerShape(14.dp)
                ) {

                    Icon(

                        Icons.Default.Visibility,

                        contentDescription = null
                    )

                    Spacer(
                        Modifier.width(6.dp)
                    )

                    Text(
                        "VIEW DETAILS"
                    )
                }

                if (

                    task.start_base != null &&

                    task.reach_client == null

                ) {

                    Button(

                        onClick = {

                            onCancel()
                        },

                        modifier =
                            Modifier.weight(0.3f),

                        colors =
                            ButtonDefaults.buttonColors(
                                containerColor =
                                    Color(0xFFD32F2F)
                            ),

                        shape =
                            RoundedCornerShape(14.dp)
                    ) {

                        Icon(

                            Icons.Default.Close,

                            contentDescription = null
                        )
                    }
                }
            }
        }
    }
}