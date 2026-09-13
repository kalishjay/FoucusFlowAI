package com.focusflow.ai

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.focusflow.ai.ui.navigation.Screen
import com.focusflow.ai.ui.navigation.bottomNavItems
import com.focusflow.ai.ui.screens.auth.AuthViewModel
import com.focusflow.ai.ui.screens.auth.LoginScreen
import com.focusflow.ai.ui.screens.auth.SignUpScreen
import com.focusflow.ai.ui.screens.auth.SplashScreen
import com.focusflow.ai.ui.screens.focus.FocusTimerScreen
import com.focusflow.ai.ui.screens.focus.FocusTimerViewModel
import com.focusflow.ai.ui.screens.home.HomeScreen
import com.focusflow.ai.ui.screens.insights.InsightsScreen
import com.focusflow.ai.ui.screens.planner.AIPlannerScreen
import com.focusflow.ai.ui.screens.profile.ProfileScreen
import com.focusflow.ai.ui.screens.tasks.TaskViewModel
import com.focusflow.ai.ui.screens.tasks.TasksScreen
import com.focusflow.ai.ui.theme.*

class MainActivity : ComponentActivity() {
    private val taskViewModel: TaskViewModel by viewModels()
    private val focusTimerViewModel: FocusTimerViewModel by viewModels()
    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            FocusFlowAITheme(darkTheme = true) {
                MainAppContainer(
                    taskViewModel = taskViewModel,
                    focusTimerViewModel = focusTimerViewModel,
                    authViewModel = authViewModel
                )
            }
        }
    }
}

@Composable
fun MainAppContainer(
    taskViewModel: TaskViewModel,
    focusTimerViewModel: FocusTimerViewModel,
    authViewModel: AuthViewModel
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val authState by authViewModel.authState.collectAsState()



    val showBottomBar = currentRoute in listOf(
        Screen.Home.route,
        Screen.Tasks.route,
        Screen.Planner.route,
        Screen.Focus.route,
        Screen.Profile.route,
        Screen.Insights.route
    )

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar(
                    containerColor = CyberDarkSurface,
                    tonalElevation = 8.dp,
                    modifier = Modifier.border(1.dp, CyberCardBorder, RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                ) {
                    bottomNavItems.forEach { screen ->
                        val isSelected = currentRoute == screen.route
                        NavigationBarItem(
                            icon = {
                                screen.icon?.let { Icon(it, contentDescription = screen.title) }
                            },
                            label = {
                                Text(
                                    text = screen.title,
                                    fontSize = 10.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                            },
                            selected = isSelected,
                            onClick = {
                                if (currentRoute != screen.route) {
                                    navController.navigate(screen.route) {
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = NeonCyan,
                                selectedTextColor = NeonCyan,
                                indicatorColor = CyberCardElevated,
                                unselectedIconColor = TextCyberSubtle,
                                unselectedTextColor = TextCyberSubtle
                            )
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Splash.route,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            composable(Screen.Splash.route) {
                SplashScreen(
                    onSplashFinished = {
                        val destination = if (authState.isLoggedIn) Screen.Home.route else Screen.Login.route
                        navController.navigate(destination) {
                            popUpTo(Screen.Splash.route) { inclusive = true }
                        }
                    }
                )
            }

            composable(Screen.Login.route) {
                LoginScreen(
                    authViewModel = authViewModel,
                    onLoginSuccess = {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    },
                    onNavigateToSignUp = {
                        navController.navigate(Screen.SignUp.route)
                    }
                )
            }

            composable(Screen.SignUp.route) {
                SignUpScreen(
                    authViewModel = authViewModel,
                    onSignUpSuccess = {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.SignUp.route) { inclusive = true }
                        }
                    },
                    onNavigateToLogin = {
                        navController.popBackStack()
                    }
                )
            }

            composable(Screen.Home.route) {
                HomeScreen(
                    taskViewModel = taskViewModel,
                    authViewModel = authViewModel,
                    onNavigateToTasks = { navController.navigate(Screen.Tasks.route) },
                    onNavigateToPlanner = { navController.navigate(Screen.Planner.route) },
                    onNavigateToFocus = { taskTitle ->
                        focusTimerViewModel.selectTask(taskTitle)
                        navController.navigate(Screen.Focus.route)
                    },
                    onNavigateToProfile = { navController.navigate(Screen.Profile.route) },
                    onNavigateToInsights = { navController.navigate(Screen.Insights.route) }
                )
            }

            composable(Screen.Tasks.route) {
                TasksScreen(
                    taskViewModel = taskViewModel,
                    onNavigateToFocus = { taskTitle ->
                        focusTimerViewModel.selectTask(taskTitle)
                        navController.navigate(Screen.Focus.route)
                    }
                )
            }

            composable(Screen.Planner.route) {
                AIPlannerScreen(
                    taskViewModel = taskViewModel,
                    onNavigateToFocus = { taskTitle ->
                        focusTimerViewModel.selectTask(taskTitle)
                        navController.navigate(Screen.Focus.route)
                    }
                )
            }

            composable(Screen.Focus.route) {
                FocusTimerScreen(
                    focusTimerViewModel = focusTimerViewModel,
                    taskViewModel = taskViewModel
                )
            }

            composable(Screen.Profile.route) {
                ProfileScreen(
                    authViewModel = authViewModel,
                    onLogout = {
                        navController.navigate(Screen.Login.route) {
                            popUpTo(0) { inclusive = true }
                        }
                    },
                    onNavigateToInsights = { navController.navigate(Screen.Insights.route) }
                )
            }

            composable(Screen.Insights.route) {
                InsightsScreen(
                    taskViewModel = taskViewModel,
                    authViewModel = authViewModel
                )
            }
        }
    }
}