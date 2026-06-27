package com.jminnovatech.mprint.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Approval
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ManagerApprovalCard(

    pendingCount: Int,

    onClick: () -> Unit

) {

    Card(

        modifier = Modifier
            .fillMaxWidth()
            .clickable {

                onClick()
            },

        shape = RoundedCornerShape(18.dp),

        colors = CardDefaults.cardColors(

            containerColor =
                Color(0xFF1E3A8A)
        )

    ) {

        Row(

            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),

            verticalAlignment =
                Alignment.CenterVertically,

            horizontalArrangement =
                Arrangement.SpaceBetween
        ) {

            Row(

                verticalAlignment =
                    Alignment.CenterVertically

            ) {

                Icon(

                    Icons.Default.Approval,

                    null,

                    tint = Color.White
                )

                Spacer(
                    Modifier.width(12.dp)
                )

                Column {

                    Text(

                        "Leave Approval",

                        color = Color.White,

                        fontWeight =
                            FontWeight.Bold,

                        fontSize = 18.sp
                    )

                    Text(

                        "Pending Requests",

                        color =
                            Color.White.copy(.7f)
                    )
                }
            }

            BadgedBox(

                badge = {

                    Badge {

                        Text(
                            pendingCount.toString()
                        )
                    }
                }

            ) {

                Icon(

                    Icons.Default.KeyboardArrowRight,

                    null,

                    tint = Color.White
                )
            }
        }
    }
}