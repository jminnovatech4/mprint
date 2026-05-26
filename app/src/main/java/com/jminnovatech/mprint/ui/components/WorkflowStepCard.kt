package com.jminnovatech.mprint.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun WorkflowStepCard(

    title: String,

    subtitle: String,

    active: Boolean,

    completed: Boolean,

    color: Color,

    expanded: Boolean = false,

    content: @Composable (() -> Unit)? = null
) {

    Card(

        modifier = Modifier.fillMaxWidth(),

        shape = RoundedCornerShape(22.dp),

        colors = CardDefaults.cardColors(
            containerColor = color
        )
    ) {

        Column(
            modifier = Modifier.padding(18.dp)
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Box(

                    modifier = Modifier
                        .size(42.dp)
                        .background(
                            if (completed)
                                Color(0xFF2563EB)
                            else
                                Color.LightGray,
                            CircleShape
                        ),

                    contentAlignment = Alignment.Center
                ) {

                    if (completed) {

                        Icon(
                            Icons.Default.Check,
                            contentDescription = null,
                            tint = Color.White
                        )

                    } else {

                        Box(
                            modifier = Modifier
                                .size(16.dp)
                                .background(
                                    Color.White,
                                    CircleShape
                                )
                        )
                    }
                }

                Spacer(Modifier.width(14.dp))

                Column {

                    Text(
                        title,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )

                    Spacer(Modifier.height(2.dp))

                    Text(
                        subtitle,
                        color = Color.Gray,
                        fontSize = 12.sp
                    )
                }
            }

            AnimatedVisibility(

                visible = expanded,

                enter = fadeIn() + expandVertically()

            ) {

                Column {

                    Spacer(Modifier.height(18.dp))

                    content?.invoke()
                }
            }
        }
    }
}