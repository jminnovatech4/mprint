package com.jminnovatech.mprint.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.DirectionsBike
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Note
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun CancelTaskDialog(

    transport: String,

    onDismiss: () -> Unit,

    onSubmit: (

        String, // reason

        String, // end odo

        String  // travel cost

    ) -> Unit
) {

    var reason by remember {
        mutableStateOf("")
    }

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
                "Cancel Task"
            )
        },

        text = {

            Column(

                modifier = Modifier
                    .verticalScroll(
                        rememberScrollState()
                    )
            ) {

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

                OutlinedTextField(

                    value = reason,

                    onValueChange = {

                        reason = it

                        error = ""
                    },

                    modifier =
                        Modifier.fillMaxWidth(),

                    label = {
                        Text("Cancel Reason")
                    },

                    leadingIcon = {

                        Icon(

                            Icons.Default.Note,

                            contentDescription = null
                        )
                    }
                )

                Spacer(
                    Modifier.height(12.dp)
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

                                it.all { c ->
                                    c.isDigit()
                                }

                            ) {

                                odo = it

                                error = ""
                            }
                        },

                        modifier =
                            Modifier.fillMaxWidth(),

                        keyboardOptions =
                            KeyboardOptions(

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

                } else {

                    OutlinedTextField(

                        value = cost,

                        onValueChange = {

                            if (

                                it.matches(
                                    Regex("^\\d*\\.?\\d*$")
                                )

                            ) {

                                cost = it

                                error = ""
                            }
                        },

                        modifier =
                            Modifier.fillMaxWidth(),

                        keyboardOptions =
                            KeyboardOptions(

                                keyboardType =
                                    KeyboardType.Decimal
                            ),

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
                }

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

                            reason.isBlank()

                        ) {

                            error =
                                "Please enter reason"

                            return@Button
                        }

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

                            transport.equals(
                                "Public",
                                true
                            )

                            &&

                            cost.isBlank()

                        ) {

                            error =
                                "Please enter Travel Cost"

                            return@Button
                        }

                        onSubmit(

                            reason,

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
                                Color.Red
                        )
                ) {

                    Icon(

                        Icons.Default.Cancel,

                        contentDescription = null
                    )

                    Spacer(
                        Modifier.width(8.dp)
                    )

                    Text(
                        "CANCEL TASK"
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

                    Text("CLOSE")
                }
            }
        }
    )
}