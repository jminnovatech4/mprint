package com.jminnovatech.mprint.ui.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Photo
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.jminnovatech.mprint.ui.components.SignaturePad

@RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
@Composable
fun CompleteWorkDialog(

    onDismiss: () -> Unit,

    onSubmit: (
        String, // problem

        String, // workDone

        String, // remarks

        String, // customerName

        String, // customerPhone

        String, // customerDesignation

        String, // signaturePath

        String, // workStatus




    ) -> Unit
) {

    var selectedStatus by remember {

        mutableStateOf("Completed")
    }

    var problem by remember {

        mutableStateOf("")
    }

    var workDone by remember {

        mutableStateOf("")
    }

    var remarks by remember {

        mutableStateOf("")
    }
    var workStatus by remember {

        mutableStateOf("Completed")
    }
    var signaturePath by remember {

        mutableStateOf("")
    }
    var customerName by remember {
        mutableStateOf("")
    }

    var customerPhone by remember {
        mutableStateOf("")
    }

    var customerDesignation by remember {
        mutableStateOf("")
    }
    Dialog(
        onDismissRequest = onDismiss
    ) {

        Card(

            shape = RoundedCornerShape(28.dp),

            modifier = Modifier.fillMaxWidth()
        ) {

            Column(

                modifier = Modifier
                    .verticalScroll(
                        rememberScrollState()
                    )
                    .padding(20.dp)
            ) {

                Text(

                    "Work Details",

                    fontSize = 24.sp,

                    fontWeight = FontWeight.Bold
                )

                Spacer(Modifier.height(20.dp))

                // STATUS
                Text(
                    text = "Work Status",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Spacer(Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    StatusCard(

                        title = "In Progress",

                        selected =
                            workStatus == "In Progress",

                        modifier =
                            Modifier.width(160.dp)

                    ) {

                        workStatus = "In Progress"
                    }

                    StatusCard(

                        title = "Completed",

                        selected =
                            workStatus == "Completed",

                        modifier =
                            Modifier.width(160.dp)

                    ) {

                        workStatus = "Completed"
                    }
                }

                Spacer(Modifier.height(10.dp))





                // PROBLEM
                Text(
                    "Problem Details",
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(Modifier.height(8.dp))

                OutlinedTextField(

                    value = problem,

                    onValueChange = {
                        problem = it
                    },

                    modifier = Modifier.fillMaxWidth(),

                    minLines = 3,

                    shape = RoundedCornerShape(16.dp),

                    placeholder = {
                        Text("Problem Details")
                    }
                )

                Spacer(Modifier.height(18.dp))

                // WORK DONE
                Text(
                    "Work Done",
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(Modifier.height(8.dp))

                OutlinedTextField(

                    value = workDone,

                    onValueChange = {
                        workDone = it
                    },

                    modifier = Modifier.fillMaxWidth(),

                    minLines = 3,

                    shape = RoundedCornerShape(16.dp),

                    placeholder = {
                        Text("Work Done")
                    }
                )

                Spacer(Modifier.height(18.dp))

                Text(
                    "Customer Details",
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(Modifier.height(12.dp))

                OutlinedTextField(

                    value = customerName,

                    onValueChange = {
                        customerName = it
                    },

                    modifier = Modifier.fillMaxWidth(),

                    label = {
                        Text("Customer Name")
                    }
                )

                Spacer(Modifier.height(10.dp))

                OutlinedTextField(

                    value = customerPhone,

                    onValueChange = {
                        customerPhone = it
                    },

                    modifier = Modifier.fillMaxWidth(),

                    label = {
                        Text("Customer Phone")
                    }
                )

                Spacer(Modifier.height(10.dp))

                OutlinedTextField(

                    value = customerDesignation,

                    onValueChange = {
                        customerDesignation = it
                    },

                    modifier = Modifier.fillMaxWidth(),

                    label = {
                        Text("Customer Designation")
                    }
                )

                Spacer(Modifier.height(20.dp))

                Spacer(Modifier.height(20.dp))

                // SIGNATURE
                Text(
                    "Customer Signature",
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(Modifier.height(10.dp))

                SignaturePad(

                    onSignatureSaved = {

                        signaturePath = it
                    }
                )

                Spacer(Modifier.height(20.dp))

                // REMARKS
                Text(
                    "Remarks",
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(Modifier.height(8.dp))

                OutlinedTextField(

                    value = remarks,

                    onValueChange = {
                        remarks = it
                    },

                    modifier = Modifier.fillMaxWidth(),

                    shape = RoundedCornerShape(16.dp),

                    placeholder = {
                        Text("Remarks")
                    }
                )

                Spacer(Modifier.height(24.dp))

                Button(

                    onClick = {

                        onSubmit(

                            selectedStatus,

                            problem,

                            workDone,

                            remarks,

                            customerName,

                            customerPhone,

                            customerDesignation,

                            signaturePath
                        )
                    },

                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),

                    shape = RoundedCornerShape(18.dp),

                    colors = ButtonDefaults.buttonColors(
                        containerColor =
                            Color(0xFF22C55E)
                    )
                ) {

                    Text(
                        "SAVE DETAILS",
                        fontSize = 16.sp
                    )
                }

                Spacer(Modifier.height(10.dp))

                OutlinedButton(

                    onClick = onDismiss,

                    modifier = Modifier.fillMaxWidth()
                ) {

                    Text("CLOSE")
                }
            }
        }
    }
}

@Composable
fun StatusButton(

    text: String,

    selected: Boolean,

    onClick: () -> Unit
) {

    Button(

        onClick = onClick,

        colors = ButtonDefaults.buttonColors(

            containerColor =
                if (selected)
                    Color(0xFFDCFCE7)
                else
                    Color(0xFFF3F4F6)
        ),

        shape = RoundedCornerShape(14.dp)
    ) {

        Text(

            text,

            color =
                if (selected)
                    Color(0xFF15803D)
                else
                    Color.Black
        )
    }
}
@Composable
fun StatusCard(

    title: String,

    selected: Boolean,

    modifier: Modifier = Modifier,

    onClick: () -> Unit
) {

    Card(

        onClick = onClick,

        modifier = modifier
            .height(72.dp),

        shape =
            RoundedCornerShape(18.dp),

        colors =
            CardDefaults.cardColors(

                containerColor =

                    if (selected)
                        Color(0xFFE8F5E9)
                    else
                        Color.White
            ),

        border =
            BorderStroke(

                2.dp,

                if (selected)
                    Color(0xFF22C55E)
                else
                    Color(0xFFE5E7EB)
            )
    ) {

        Row(

            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 5.dp),

            verticalAlignment =
                Alignment.CenterVertically
        ) {

            RadioButton(

                selected = selected,

                onClick = null
            )

            Spacer(
                Modifier.width(8.dp)
            )

            Text(

                text = title,

                fontSize = 12.sp,

                fontWeight =
                    FontWeight.Bold,

                maxLines = 1
            )
        }
    }
}

