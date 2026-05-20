    package com.jminnovatech.mprint.ui.screens

    import DashboardCard
    import androidx.compose.animation.core.LinearEasing
    import androidx.compose.animation.core.animateFloat
    import androidx.compose.animation.core.infiniteRepeatable
    import androidx.compose.animation.core.rememberInfiniteTransition
    import androidx.compose.animation.core.tween
    import androidx.compose.foundation.background
    import androidx.compose.runtime.*
    import androidx.compose.material3.*
    import androidx.compose.foundation.layout.*
    import androidx.compose.foundation.shape.RoundedCornerShape
    import androidx.compose.material.icons.Icons
    import androidx.compose.material.icons.filled.Menu
    import androidx.compose.material.icons.filled.Person
    import androidx.compose.material.icons.filled.Refresh
    import androidx.compose.material.icons.filled.Warning
    import androidx.compose.ui.Alignment
    import androidx.compose.ui.Modifier
    import androidx.compose.ui.draw.rotate
    import androidx.compose.ui.graphics.Brush
    import androidx.compose.ui.graphics.Color
    import androidx.compose.ui.platform.LocalContext
    import androidx.compose.ui.text.font.FontWeight
    import androidx.compose.ui.unit.dp
    import androidx.compose.ui.unit.sp
    import androidx.compose.ui.window.Dialog
    import androidx.lifecycle.viewmodel.compose.viewModel
    import androidx.navigation.NavHostController
    import androidx.navigation.compose.rememberNavController
    import com.jminnovatech.core.datastore.SessionManager
    import com.jminnovatech.core.utils.Resource
    import com.jminnovatech.mprint.viewmodel.MainViewModel
    import com.jminnovatech.mprint.ui.components.DrawerMenu
    import com.jminnovatech.mprint.ui.components.Loader
    import kotlinx.coroutines.launch

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun HomeScreen(navController: NavHostController,onLogout: () -> Unit) {

        val drawerState = rememberDrawerState(DrawerValue.Closed)
        val scope = rememberCoroutineScope()
        val vm: MainViewModel = viewModel()

        val context = LocalContext.current
        val session = SessionManager(context)

        // 🔥 MENU STATE
        var selectedMenu by remember { mutableStateOf("Home") }

        val infiniteTransition = rememberInfiniteTransition(label = "")

        var isRefreshing by remember { mutableStateOf(false) }

        val rotation by infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 360f,   // ✅ FIXED
            animationSpec = infiniteRepeatable(
                animation = tween(800, easing = LinearEasing)
            ),
            label = ""
        )

// 🔥 STOP REFRESH
        LaunchedEffect(vm.profileState) {
            if (vm.profileState !is Resource.Loading) {
                isRefreshing = false
            }
        }

        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                DrawerMenu { item ->

                    scope.launch { drawerState.close() }

                    when (item) {
                        "Logout" -> {
                            session.clearSession()   // ✅ FIX LOGOUT
                            onLogout()
                        }
                        else -> {
                            selectedMenu = item   // ✅ SWITCH SCREEN
                        }
                    }
                }
            }
        ) {

            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text("Mprint 🚀") },

                        navigationIcon = {
                            IconButton(onClick = {
                                scope.launch { drawerState.open() }
                            }) {
                                Icon(Icons.Default.Menu, contentDescription = null)
                            }
                        },

                        actions = {

                            IconButton(
                                onClick = {
                                    isRefreshing = true
                                    val token = session.getToken() ?: ""
                                    vm.refreshAll(token)

                                }
                            ) {

                                Icon(
                                    Icons.Default.Refresh,
                                    contentDescription = "Refresh",
                                    modifier = if (isRefreshing) {
                                        Modifier.rotate(rotation)   // ✅ smooth spin
                                    } else Modifier
                                )
                            }
                        }
                    )
                }
            ) { padding ->

                Column(
                    Modifier
                        .padding(padding)
                        .padding(5.dp)
                ) {

                    // 🔥 SCREEN SWITCHING
                    when (selectedMenu) {

                        // 🏠 HOME SCREEN
                        "Home" -> {

                            ProfileSection(vm, navController, onLogout)

                            Spacer(Modifier.height(1.dp))
                            AttendanceScreen(
                                vm = vm,
                                navController = navController
                            )
                            Row(
                                Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {



                            }



                        }

//                        // 📍 ATTENDANCE SCREEN
//                        "Attendance" -> {
//                            AttendanceScreen(vm)
//                        }
//
//                        // 📊 REPORT SCREEN
//                        "Report" -> {
//                            Text("Report Coming Soon")
//                        }
                    }
                }
            }
        }
    }

    //////////////////////////////////////////////////////////////////
    // ✅ PROFILE SECTION (SEPARATE FUNCTION — FIXED)
    //////////////////////////////////////////////////////////////////
    @Composable
    fun ProfileSection(vm: MainViewModel, navController: NavHostController, onLogout: () -> Unit) {
        var showSessionDialog by remember { mutableStateOf(false) }
        val context = LocalContext.current
        val session = SessionManager(context)

        val token = session.getToken() ?: ""
        val state = vm.profileState

        // 🔥 API CALL
        LaunchedEffect(token) {
            if (token.isNotEmpty()) {
                vm.loadProfile(token)
            }
        }



        LaunchedEffect(state) {
            if (state is Resource.Error && state.message == "SESSION_EXPIRED") {
                showSessionDialog = true   // 🔥 শুধু dialog show
            }
        }
        when (state) {

            is Resource.Loading -> {
                Loader()
            }

            is Resource.Success -> {

                val data = state.data.data

                // 🔥 IMPORTANT: SERVER → CHECK-IN SYNC
                LaunchedEffect(data.today_attendance?.attendance_in_time) {
                    data.today_attendance?.attendance_in_time?.let {
                        vm.setCheckInFromServer(it)
                    }
                }


            }

            is Resource.Error -> {
                showSessionDialog = true

            }
            else -> {}
        }
// 🔥 SESSION EXPIRED DIALOG
        if (showSessionDialog) {

            val logoutState = vm.logoutState

            Dialog(onDismissRequest = { }) {

                Card(
                    shape = RoundedCornerShape(24.dp),
                    elevation = CardDefaults.cardElevation(12.dp)
                ) {

                    Column(
                        modifier = Modifier
                            .background(Color.White)
                            .padding(20.dp)
                    ) {

                        // 🔥 HEADER
                        Row(verticalAlignment = Alignment.CenterVertically) {

                            Icon(
                                Icons.Default.Warning,
                                contentDescription = null,
                                tint = Color(0xFFD32F2F),
                                modifier = Modifier.size(36.dp)
                            )

                            Spacer(Modifier.width(10.dp))

                            Text(
                                "Session Expired",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFD32F2F)
                            )
                        }

                        Spacer(Modifier.height(12.dp))

                        Text(
                            "Connectivity issue or session expired.",
                            color = Color.DarkGray,
                            fontSize = 14.sp
                        )

                        Spacer(Modifier.height(24.dp))

                        // 🔥 BUTTON AREA
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.CenterEnd
                        ) {

                            Button(
                                onClick = {
                                    val token = session.getToken() ?: ""
                                    vm.logout(token)   // 🔥 API CALL
                                },
                                enabled = logoutState !is Resource.Loading,
                                shape = RoundedCornerShape(50.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFFD32F2F)
                                )
                            ) {

                                if (logoutState is Resource.Loading) {

                                    CircularProgressIndicator(
                                        color = Color.White,
                                        strokeWidth = 2.dp,
                                        modifier = Modifier.size(18.dp)
                                    )

                                    Spacer(Modifier.width(8.dp))

                                    Text("Logging out...", color = Color.White)

                                } else {
                                    Text("Login Again", color = Color.White)
                                }
                            }
                        }
                    }
                }
            }

            // 🔥 LOGOUT RESPONSE HANDLE
            when (logoutState) {

                is Resource.Success -> {
                    LaunchedEffect(Unit) {
                        showSessionDialog = false
                        session.clearSession()
                        onLogout()
                    }
                }

                is Resource.Error -> {
                    LaunchedEffect(Unit) {
                        // even if API fail → force logout
                        showSessionDialog = false
                        session.clearSession()
                        onLogout()
                    }
                }

                else -> {}
            }
        }
    }