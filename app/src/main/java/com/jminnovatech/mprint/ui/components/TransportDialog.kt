package com.jminnovatech.mprint.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DirectionsBike
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog

@Composable
fun TransportDialog(

    onDismiss: () -> Unit,

    onSubmit: (

        transport: String,
        odo: String

    ) -> Unit
) {

    var transport by remember {
        mutableStateOf("Bike")
    }

    var odo by remember {
        mutableStateOf("")
    }

    Dialog(
        onDismissRequest = onDismiss
    ) {

        Card(

            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),

            shape = RoundedCornerShape(28.dp)
        ) {

            Column(

                modifier = Modifier
                    .background(Color.White)
                    .verticalScroll(rememberScrollState())
                    .padding(22.dp)
            ) {

                // HEADER
                Row(

                    modifier = Modifier.fillMaxWidth(),

                    horizontalArrangement =
                        Arrangement.SpaceBetween,

                    verticalAlignment =
                        Alignment.CenterVertically
                ) {

                    Text(

                        "Start Journey",

                        fontSize = 22.sp,

                        fontWeight = FontWeight.Bold
                    )

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

                Text(
                    "Select Transport",
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(Modifier.height(14.dp))

                Row(
                    horizontalArrangement =
                        Arrangement.spacedBy(14.dp)
                ) {

                    // BIKE
                    Card(

                        modifier = Modifier
                            .weight(1f)
                            .clickable {
                                transport = "Bike"
                            },

                        colors = CardDefaults.cardColors(

                            if (transport == "Bike")
                                Color(0xFFDCEBFF)
                            else
                                Color(0xFFF5F5F5)
                        ),

                        shape = RoundedCornerShape(20.dp)
                    ) {

                        Column(

                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp),

                            horizontalAlignment =
                                Alignment.CenterHorizontally
                        ) {

                            Icon(
                                Icons.Default.DirectionsBike,
                                contentDescription = null,
                                tint = Color(0xFF2563EB),
                                modifier = Modifier.size(40.dp)
                            )

                            Spacer(Modifier.height(10.dp))

                            Text(
                                "Bike",
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    // PUBLIC
                    Card(

                        modifier = Modifier
                            .weight(1f)
                            .clickable {
                                transport = "Public"
                            },

                        colors = CardDefaults.cardColors(

                            if (transport == "Public")
                                Color(0xFFDCEBFF)
                            else
                                Color(0xFFF5F5F5)
                        ),

                        shape = RoundedCornerShape(20.dp)
                    ) {

                        Column(

                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp),

                            horizontalAlignment =
                                Alignment.CenterHorizontally
                        ) {

                            Icon(
                                Icons.Default.DirectionsBus,
                                contentDescription = null,
                                tint = Color(0xFF2563EB),
                                modifier = Modifier.size(40.dp)
                            )

                            Spacer(Modifier.height(10.dp))

                            Text(
                                "Public",
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Spacer(Modifier.height(24.dp))

                if (transport == "Bike") {

                    OutlinedTextField(

                        value = odo,

                        onValueChange = {
                            odo = it
                        },

                        modifier = Modifier.fillMaxWidth(),

                        shape = RoundedCornerShape(18.dp),

                        label = {
                            Text("Enter Start ODO")
                        }
                    )

                    Spacer(Modifier.height(20.dp))
                }

                Button(

                    onClick = {

                        onSubmit(
                            transport,
                            odo
                        )
                    },

                    modifier = Modifier
                        .fillMaxWidth()
                        .height(58.dp),

                    shape = RoundedCornerShape(18.dp),

                    colors = ButtonDefaults.buttonColors(
                        containerColor =
                            Color(0xFF16A34A)
                    )
                ) {

                    Text(
                        "START TASK",
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}