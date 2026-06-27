package com.jminnovatech.mprint.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.jminnovatech.core.model.ManagerLeaveResponse
import com.jminnovatech.core.utils.Resource
import com.jminnovatech.core.utils.formatServerDate


@Composable
fun LeaveApprovalDialog(

    onClose: () -> Unit,
    state: Resource<ManagerLeaveResponse>?,
    onApprove: (Int) -> Unit,
    onReject: (Int) -> Unit
){



    Dialog(

        onDismissRequest = onClose

    ){

        Card(

            modifier = Modifier

                .fillMaxWidth()

                .fillMaxHeight(.92f),

            shape = RoundedCornerShape(24.dp)

        ){

            Column(

                Modifier.fillMaxSize()

            ){

                Row(

                    modifier = Modifier

                        .fillMaxWidth()

                        .background(Color(0xFF1565C0))

                        .padding(18.dp),

                    verticalAlignment = Alignment.CenterVertically,

                    horizontalArrangement = Arrangement.SpaceBetween

                ){

                    Column{

                        Text(

                            "Leave Approval",

                            color = Color.White,

                            fontWeight = FontWeight.Bold,

                            fontSize = 22.sp
                        )

                        Text(

                            "Pending Requests",

                            color = Color.White.copy(.8f)
                        )
                    }

                    IconButton(

                        onClick = onClose

                    ){

                        Icon(

                            Icons.Default.Close,

                            null,

                            tint = Color.White
                        )
                    }
                }

                Spacer(Modifier.height(10.dp))

                when (state) {

                    is Resource.Loading -> {

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(40.dp),
                            contentAlignment = Alignment.Center
                        ) {

                            CircularProgressIndicator()
                        }
                    }

                    is Resource.Error -> {

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(30.dp),
                            contentAlignment = Alignment.Center
                        ) {

                            Text(
                                state.message ?: "Something went wrong",
                                color = Color.Red
                            )
                        }
                    }

                    is Resource.Success -> {

                        val list = state.data.data

                        if (list.isEmpty()) {

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(40.dp),
                                contentAlignment = Alignment.Center
                            ) {

                                Text(
                                    "No Pending Leave",
                                    fontWeight = FontWeight.Bold
                                )
                            }

                        } else {

                            LazyColumn(

                                modifier = Modifier.weight(1f),

                                contentPadding = PaddingValues(16.dp),

                                verticalArrangement = Arrangement.spacedBy(12.dp)

                            ) {

                                items(list) { leave ->

                                    Card(

                                        elevation =
                                            CardDefaults.cardElevation(5.dp),

                                        shape =
                                            RoundedCornerShape(18.dp)

                                    ) {

                                        Column(

                                            Modifier.padding(16.dp)

                                        ) {

                                            Row(

                                                verticalAlignment =
                                                    Alignment.CenterVertically

                                            ) {

                                                Icon(

                                                    Icons.Default.Person,

                                                    null,

                                                    tint = Color(0xFF1565C0)
                                                )

                                                Spacer(
                                                    Modifier.width(10.dp)
                                                )

                                                Column {

                                                    Text(

                                                        leave.employee.name,

                                                        fontWeight =
                                                            FontWeight.Bold,

                                                        fontSize = 18.sp
                                                    )

                                                    Text(

                                                        leave.employee.designation
                                                            ?: ""
                                                    )
                                                }
                                            }

                                            Spacer(
                                                Modifier.height(12.dp)
                                            )

                                            Text(
                                                "Leave Type : ${leave.leave_type}"
                                            )

                                            Text(
                                                "Days : ${leave.days}"
                                            )

                                            Text(
                                                "From : ${formatServerDate(leave.from_date)}"
                                            )

                                            Text(
                                                "To : ${formatServerDate(leave.to_date)}"
                                            )

                                            Spacer(
                                                Modifier.height(6.dp)
                                            )

                                            Text(

                                                leave.reason ?: "-"

                                            )

                                            Spacer(
                                                Modifier.height(18.dp)
                                            )

                                            Row(

                                                horizontalArrangement =
                                                    Arrangement.spacedBy(10.dp)

                                            ) {

                                                Button(

                                                    modifier =
                                                        Modifier.weight(1f),

                                                    onClick = {

                                                        onApprove(
                                                            leave.id
                                                        )

                                                    }

                                                ) {

                                                    Text("APPROVE")
                                                }

                                                Button(

                                                    modifier =
                                                        Modifier.weight(1f),

                                                    colors =
                                                        ButtonDefaults.buttonColors(
                                                            Color.Red
                                                        ),

                                                    onClick = {

                                                        onReject(
                                                            leave.id
                                                        )

                                                    }

                                                ) {

                                                    Text("REJECT")
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    else -> {}
                }

                OutlinedButton(

                    onClick = onClose,

                    modifier = Modifier

                        .fillMaxWidth()

                        .padding(16.dp)

                ){

                    Text("CLOSE")
                }
            }
        }
    }
}