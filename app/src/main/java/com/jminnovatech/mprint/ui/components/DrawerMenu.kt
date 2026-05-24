package com.jminnovatech.mprint.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DrawerMenu(onClick: (String) -> Unit) {

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .width(260.dp)
            .background(Color.White)
            .padding(16.dp)
    ) {

        Text("Mprint 🚀", fontSize = 22.sp, color = Color.Black)

        Spacer(Modifier.height(30.dp))

        DrawerItem("🏠 Home") { onClick("Home") }
        DrawerItem("📍 Attendance") { onClick("Attendance") }
        DrawerItem("📝 Apply Leave") {
            onClick("leave")
        }
        DrawerItem("📊 Report") { onClick("Report") }
        DrawerItem("🚪 Logout") { onClick("Logout") }
    }
}

@Composable
fun DrawerItem(title: String, onClick: () -> Unit) {

    TextButton(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(title, fontSize = 16.sp)
    }
}