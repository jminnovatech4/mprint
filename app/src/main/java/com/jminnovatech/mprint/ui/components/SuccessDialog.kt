package com.jminnovatech.mprint.ui.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun SuccessDialog(msg: String, onClose: () -> Unit) {
    AlertDialog(
        onDismissRequest = onClose,
        confirmButton = {
            Button(onClick = onClose) {
                Text("OK")
            }
        },
        title = { Text("Success ✅") },
        text = { Text(msg) }
    )
}