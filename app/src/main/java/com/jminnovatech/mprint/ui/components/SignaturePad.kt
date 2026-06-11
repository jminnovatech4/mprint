package com.jminnovatech.mprint.ui.components

import android.graphics.Bitmap
import android.graphics.Paint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import java.io.File
import java.io.FileOutputStream
import androidx.compose.animation.scaleIn
@RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
@Composable
fun SignaturePad(

    onSignatureSaved: (
        String
    ) -> Unit
) {

    val context = LocalContext.current

    val points = remember {

        mutableStateListOf<Pair<Offset, Boolean>>()
    }

    var saved by remember {

        mutableStateOf(false)
    }

    Card(

        modifier = Modifier.fillMaxWidth(),

        shape = RoundedCornerShape(26.dp),

        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),

        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        )
    ) {

        Column(

            modifier = Modifier.padding(18.dp)
        ) {

            Text(

                text = "Customer Signature",

                style =
                    MaterialTheme.typography.titleMedium,

                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(14.dp))

            Box(

                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp)
                    .border(
                        width = 2.dp,
                        color = Color(0xFFE2E8F0),
                        shape =
                            RoundedCornerShape(22.dp)
                    )
                    .background(
                        Color.White,
                        RoundedCornerShape(22.dp)
                    )
            ) {

                Canvas(

                    modifier = Modifier
                        .fillMaxSize()
                        .pointerInput(Unit) {

                            detectDragGestures(

                                onDragStart = {

                                    saved = false

                                    points.add(
                                        Pair(it, false)
                                    )
                                },

                                onDrag = { change, _ ->

                                    points.add(
                                        Pair(
                                            change.position,
                                            true
                                        )
                                    )
                                }
                            )
                        }
                ) {

                    for (

                    i in 1 until points.size

                    ) {

                        if (
                            points[i].second
                        ) {

                            drawLine(

                                color = Color.Black,

                                start =
                                    points[i - 1].first,

                                end =
                                    points[i].first,

                                strokeWidth = 6f
                            )
                        }
                    }
                }

                androidx.compose.animation.AnimatedVisibility(

                    visible = saved,

                    enter = scaleIn()

                ) {

                    Box(

                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Color(0x66000000)
                            ),

                        contentAlignment =
                            Alignment.Center
                    ) {

                        Column(

                            horizontalAlignment =
                                Alignment.CenterHorizontally
                        ) {

                            Icon(

                                Icons.Default.CheckCircle,

                                contentDescription = null,

                                tint = Color(0xFF22C55E),

                                modifier =
                                    Modifier.size(82.dp)
                            )

                            Spacer(
                                Modifier.height(10.dp)
                            )

                            Text(

                                text =
                                    "Signature Saved",

                                color = Color.White,

                                style =
                                    MaterialTheme.typography.titleMedium
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(18.dp))


            Column {

                Row(

                    modifier = Modifier.fillMaxWidth(),

                    horizontalArrangement =
                        Arrangement.spacedBy(10.dp)
                ) {

                    ElevatedButton(

                        onClick = {

                            if (
                                points.isNotEmpty()
                            ) {

                                points.removeLast()
                            }
                        },

                        modifier = Modifier.weight(1f),

                        shape =
                            RoundedCornerShape(18.dp),

                        colors =
                            ButtonDefaults.elevatedButtonColors(
                                containerColor =
                                    Color(0xFFF8FAFC)
                            )
                    ) {

                        Text(
                            "UNDO"
                        )
                    }

                    OutlinedButton(

                        onClick = {

                            points.clear()

                            saved = false
                        },

                        modifier = Modifier.weight(1f),

                        shape =
                            RoundedCornerShape(18.dp)
                    ) {



                        Spacer(Modifier.width(6.dp))

                        Text("CLEAR")
                    }
                }

                Spacer(Modifier.height(12.dp))

                Button(

                    onClick = {

                        if (
                            points.isEmpty()
                        ) return@Button

                        val bitmap =
                            Bitmap.createBitmap(
                                1200,
                                700,
                                Bitmap.Config.ARGB_8888
                            )

                        val canvas =
                            android.graphics.Canvas(bitmap)

                        canvas.drawColor(
                            android.graphics.Color.WHITE
                        )

                        val paint =
                            Paint().apply {

                                color =
                                    android.graphics.Color.BLACK

                                strokeWidth = 8f

                                style =
                                    Paint.Style.STROKE

                                isAntiAlias = true
                            }

                        for (
                        i in 1 until points.size
                        ) {

                            if (
                                points[i].second
                            ) {

                                canvas.drawLine(

                                    points[i - 1].first.x,
                                    points[i - 1].first.y,

                                    points[i].first.x,
                                    points[i].first.y,

                                    paint
                                )
                            }
                        }

                        val file = File(

                            context.cacheDir,

                            "signature_${
                                System.currentTimeMillis()
                            }.png"
                        )

                        val stream =
                            FileOutputStream(file)

                        bitmap.compress(

                            Bitmap.CompressFormat.PNG,

                            100,

                            stream
                        )

                        stream.flush()

                        stream.close()

                        saved = true

                        onSignatureSaved(
                            file.absolutePath
                        )
                    },

                    enabled = points.isNotEmpty(),

                    modifier = Modifier
                        .fillMaxWidth()
                        .height(58.dp),

                    shape = RoundedCornerShape(18.dp),

                    colors =
                        ButtonDefaults.buttonColors(
                            containerColor =
                                Color(0xFF6D4CBB)
                        )
                ) {

                    Text(
                        "SAVE SIGNATURE"
                    )
                }
            }


        }
    }
}