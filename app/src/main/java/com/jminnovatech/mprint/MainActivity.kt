package com.jminnovatech.mprint

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.navigation.compose.*
import com.jminnovatech.mprint.ui.screens.*
import com.jminnovatech.mprint.viewmodel.MainViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

class MainActivity : ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            val navController = rememberNavController()

            val vm: MainViewModel = viewModel()

            NavHost(
                navController = navController,
                startDestination = "splash"
            ) {

                // 🔥 SPLASH
                composable("splash") {

                    SplashScreen(navController)
                }

                // 🔐 LOGIN
                composable("login") {

                    LoginScreen {

                        navController.navigate("home") {

                            popUpTo("login") {
                                inclusive = true
                            }
                        }
                    }
                }

                // 🏠 HOME
                composable("home") {

                    HomeScreen(

                        navController = navController,

                        onLogout = {

                            navController.navigate("login") {

                                popUpTo("home") {
                                    inclusive = true
                                }
                            }
                        }
                    )
                }

                // 🔥 LEAVE
                composable("leave") {

                    LeaveScreen(

                        vm = vm,

                        onMenuClick = {

                            navController.popBackStack()
                        }
                    )
                }
            }
        }
    }
}