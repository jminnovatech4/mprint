package com.jminnovatech.mprint

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.*
import com.jminnovatech.mprint.ui.screens.*

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            val navController = rememberNavController()

            NavHost(
                navController = navController,
                startDestination = "splash"
            ) {

                // 🔥 SPLASH (ENTRY POINT)
                composable("splash") {
                    SplashScreen(navController)
                }

                // 🔐 LOGIN (KEEP YOUR EXISTING DESIGN)
                composable("login") {
                    LoginScreen {
                        navController.navigate("home") {
                            popUpTo("login") { inclusive = true }
                        }
                    }
                }

                // 🏠 HOME (KEEP YOUR EXISTING UI)
                composable("home") {
                    HomeScreen(
                        navController = navController,
                        onLogout = {
                            navController.navigate("login") {
                                popUpTo("home") { inclusive = true }
                            }
                        }
                    )
                }
                composable("leave") {
                    LeaveScreen(
                        vm = androidx.lifecycle.viewmodel.compose.viewModel()
                    )
                }
            }
        }
    }
}