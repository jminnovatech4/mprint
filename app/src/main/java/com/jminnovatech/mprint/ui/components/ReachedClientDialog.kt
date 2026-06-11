package com.jminnovatech.mprint.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.jminnovatech.core.model.TaskItem

@Composable
fun ReachedClientDialog(
    task: TaskItem,
    onDismiss: () -> Unit,

    onSubmit: (String) -> Unit
) {

    var odo by remember {
        mutableStateOf("")
    }
    var odoError by remember {
        mutableStateOf(false)
    }
    Dialog(
        onDismissRequest = onDismiss
    ) {

        Card(
            shape = RoundedCornerShape(26.dp)
        ) {

            Column(

                modifier = Modifier
                    .padding(22.dp)
                    .verticalScroll(
                        rememberScrollState()
                    )
            ) {

                Text(
                    "Reached Client",
                    style =
                        MaterialTheme.typography
                            .headlineSmall
                )

                Spacer(Modifier.height(20.dp))

                if (
                    task.mode_of_transport == "Bike"
                ) {

                    OutlinedTextField(

                        value = odo,

                        onValueChange = {

                            odo = it

                            odoError = false
                        },

                        isError = odoError,

                        modifier =
                            Modifier.fillMaxWidth(),

                        label = {
                            Text("Reach ODO")
                        },

                        leadingIcon = {

                            Icon(
                                Icons.Default.LocationOn,
                                contentDescription = null
                            )
                        }
                    )
                    if (odoError) {

                        Text(

                            text = "Reach ODO is required",

                            color =
                                MaterialTheme.colorScheme.error,

                            style =
                                MaterialTheme.typography.bodySmall
                        )
                    }
                    Spacer(Modifier.height(18.dp))
                }

                Spacer(Modifier.height(24.dp))

                Button(

                    onClick = {

                        if (

                            task.mode_of_transport == "Bike"

                            &&

                            odo.isBlank()

                        ) {

                            odoError = true

                            return@Button
                        }

                        onSubmit(

                            if (
                                task.mode_of_transport == "Bike"
                            )
                                odo
                            else
                                ""
                        )
                    },

                    modifier =
                        Modifier.fillMaxWidth(),

                    shape =
                        RoundedCornerShape(18.dp)
                ) {

                    Text("MARK AS REACHED")
                }

                Spacer(Modifier.height(10.dp))

                OutlinedButton(

                    onClick = onDismiss,

                    modifier =
                        Modifier.fillMaxWidth()
                ) {

                    Text("CLOSE")
                }
            }
        }
    }
}