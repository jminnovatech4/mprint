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
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
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
    var odoError by remember {
        mutableStateOf(false)
    }
    var odo by remember {
        mutableStateOf("")
    }
    val context = LocalContext.current
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

                            odoError = false
                        },

                        isError = odoError,

                        modifier = Modifier.fillMaxWidth(),

                        shape = RoundedCornerShape(18.dp),

                        label = {
                            Text("Enter Start ODO")
                        }
                    )
                    if (odoError) {

                        Text(

                            text = "Start ODO is required",

                            color = MaterialTheme.colorScheme.error,

                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                    Spacer(Modifier.height(20.dp))
                }

                Button(

                    onClick = {

                        if (

                            transport == "Bike"

                            &&

                            odo.isBlank()

                        ) {

                            odoError = true

                            return@Button
                        }

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