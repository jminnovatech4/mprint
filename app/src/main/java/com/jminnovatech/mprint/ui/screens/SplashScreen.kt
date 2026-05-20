package com.jminnovatech.mprint.ui.screens

import androidx.compose.foundation.background
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import com.jminnovatech.core.datastore.SessionManager

@Composable
fun SplashScreen(nav: NavController) {

    val context = LocalContext.current
    val session = SessionManager(context)

    LaunchedEffect(Unit) {

        delay(1200)

        if (session.isLoggedIn()) {
            nav.navigate("home") {
                popUpTo("splash") { inclusive = true }
            }
        } else {
            nav.navigate("login") {
                popUpTo("splash") { inclusive = true }
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("MPRINT 🚀", fontSize = 26.sp)
    }
}