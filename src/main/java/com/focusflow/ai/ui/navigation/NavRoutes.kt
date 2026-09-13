package com.focusflow.ai.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Timer
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val title: String, val icon: ImageVector? = null) {
    object Splash : Screen("splash", "Splash")
    object Login : Screen("login", "Login")
    object SignUp : Screen("signup", "Sign Up")

    object Home : Screen("home", "Flow", Icons.Default.Dashboard)
    object Tasks : Screen("tasks", "Tasks", Icons.Default.CheckCircle)
    object Planner : Screen("planner", "AI Plan", Icons.Default.AutoAwesome)
    object Focus : Screen("focus", "Focus", Icons.Default.Timer)
    object Profile : Screen("profile", "Profile", Icons.Default.Person)
    object Insights : Screen("insights", "Insights")
}

val bottomNavItems = listOf(
    Screen.Home,
    Screen.Tasks,
    Screen.Planner,
    Screen.Focus,
    Screen.Profile
)