package com.jminnovatech.mprint.ui.screens

import android.provider.Settings
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.material3.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jminnovatech.core.datastore.SessionManager
import com.jminnovatech.mprint.viewmodel.MainViewModel
import com.jminnovatech.core.utils.Resource
import com.jminnovatech.mprint.ui.components.Loader
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
@Composable
fun LoginScreen(onSuccess: () -> Unit) {

    val context = LocalContext.current
    val session = SessionManager(context)
    val vm: MainViewModel = viewModel()

    var id by remember { mutableStateOf("") }
    var pass by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    val deviceId = Settings.Secure.getString(
        context.contentResolver,
        Settings.Secure.ANDROID_ID
    )

    val state = vm.loginState

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFF0F172A), Color(0xFF1E293B))
                )
            ),
        contentAlignment = Alignment.Center
    ) {

        Card(
            shape = RoundedCornerShape(24.dp),
            elevation = CardDefaults.cardElevation(12.dp),
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth()
        ) {

            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    "Welcome Back 👋",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(Modifier.height(20.dp))

                // 🔥 ID FIELD
                OutlinedTextField(
                    value = id,
                    onValueChange = { id = it },
                    label = { Text("User ID") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(Modifier.height(12.dp))

                // 🔥 PASSWORD FIELD WITH SHOW/HIDE
                OutlinedTextField(
                    value = pass,
                    onValueChange = { pass = it },
                    label = { Text("Password") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    visualTransformation = if (passwordVisible)
                        VisualTransformation.None
                    else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = {
                            passwordVisible = !passwordVisible
                        }) {
                            Icon(
                                imageVector = if (passwordVisible)
                                    Icons.Default.Visibility
                                else Icons.Default.VisibilityOff,
                                contentDescription = null
                            )
                        }
                    }
                )

                Spacer(Modifier.height(20.dp))

                // 🔥 LOGIN BUTTON (PREMIUM)
                Button(
                    onClick = {
                        vm.login(id, pass, deviceId)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(55.dp),
                    shape = RoundedCornerShape(30.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF1976D2)
                    )
                ) {
                    Text("Login 🚀", fontSize = 16.sp)
                }
            }
        }

        // 🔥 STATE HANDLING (UNCHANGED)
        when (state) {

            is Resource.Loading -> Loader()

            is Resource.Success -> {

                val data = state.data

                LaunchedEffect(data) {

                    if (data.status && !data.token.isNullOrEmpty()) {

                        session.saveToken(data.token)
                        onSuccess()

                    } else {
                        Toast.makeText(
                            context,
                            data.message ?: "Login Failed",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }

            is Resource.Error -> {
                Toast.makeText(context, state.message, Toast.LENGTH_SHORT).show()
            }

            else -> {}
        }
    }
}