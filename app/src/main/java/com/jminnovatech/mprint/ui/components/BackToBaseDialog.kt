
package com.jminnovatech.mprint.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsBike
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
@Composable
fun BackToBaseDialog(

    transport: String,

    onDismiss: () -> Unit,

    onSubmit: (
        endOdo: String,
        travelCost: String
    ) -> Unit
) {

    var odo by remember {
        mutableStateOf("")
    }

    var cost by remember {
        mutableStateOf("")
    }

    var error by remember {
        mutableStateOf("")
    }

    AlertDialog(

        onDismissRequest = onDismiss,

        confirmButton = {},

        title = {

            Text(
                "Return To Base"
            )
        },

        text = {

            Column {

                Card(

                    colors =
                        CardDefaults.cardColors(
                            containerColor =
                                Color(0xFFF8FAFC)
                        )
                ) {

                    Column(

                        modifier =
                            Modifier.padding(12.dp)
                    ) {

                        Text(
                            "Transport : $transport"
                        )
                    }
                }

                Spacer(
                    Modifier.height(16.dp)
                )

                if (
                    transport.equals(
                        "Bike",
                        true
                    )
                ) {

                    OutlinedTextField(

                        value = odo,

                        onValueChange = {

                            if (

                                it.all { ch ->

                                    ch.isDigit()
                                }

                            ) {

                                odo = it

                                error = ""
                            }
                        },

                        modifier =
                            Modifier.fillMaxWidth(),

                        keyboardOptions = KeyboardOptions(

                            keyboardType =
                                KeyboardType.Number
                        ),

                        label = {
                            Text("End ODO")
                        },

                        leadingIcon = {

                            Icon(

                                Icons.Default.DirectionsBike,

                                contentDescription = null
                            )
                        }
                    )

                    Spacer(
                        Modifier.height(12.dp)
                    )
                }

                OutlinedTextField(

                    value = cost,

                    onValueChange = {

                        cost = it

                        error = ""
                    },

                    modifier =
                        Modifier.fillMaxWidth(),

                    label = {
                        Text("Travel Cost")
                    },

                    leadingIcon = {

                        Icon(

                            Icons.Default.Payments,

                            contentDescription = null
                        )
                    }
                )

                if (
                    error.isNotEmpty()
                ) {

                    Spacer(
                        Modifier.height(8.dp)
                    )

                    Text(

                        text = error,

                        color = Color.Red
                    )
                }

                Spacer(
                    Modifier.height(20.dp)
                )

                Button(

                    onClick = {

                        if (

                            transport.equals(
                                "Bike",
                                true
                            )

                            &&

                            odo.isBlank()

                        ) {

                            error =
                                "Please enter End ODO"

                            return@Button
                        }

                        if (

                            cost.isBlank()

                        ) {

                            error =
                                "Please enter Travel Cost"

                            return@Button
                        }

                        onSubmit(

                            odo,

                            cost
                        )
                    },

                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .height(56.dp),

                    shape =
                        RoundedCornerShape(16.dp),

                    colors =
                        ButtonDefaults.buttonColors(
                            containerColor =
                                Color(0xFF16A34A)
                        )
                ) {

                    Icon(

                        Icons.Default.Home,

                        contentDescription = null
                    )

                    Spacer(
                        Modifier.width(8.dp)
                    )

                    Text(
                        "RETURN TO BASE"
                    )
                }

                Spacer(
                    Modifier.height(10.dp)
                )

                OutlinedButton(

                    onClick = onDismiss,

                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .height(52.dp),

                    shape =
                        RoundedCornerShape(16.dp)
                ) {

                    Text("CANCEL")
                }
            }
        }
    )
}

