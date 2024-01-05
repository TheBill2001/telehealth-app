package it.app.telehealth.ui.screens

import android.annotation.SuppressLint
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

sealed class NavigationScreen(val route: String, val title: String/*, @DrawableRes val icon: Int*/) {
    object HomeScreen : NavigationScreen(
        route = "home_screen",
        title = "Home",
//        icon = R.drawable.ic_home
    )

//    object ProfileScreen :
//        NavigationScreen(route = "profile_screen", title = "Profile"/*, icon = R.drawable.ic_chat*/)

//    object SettingsScreen : NavigationScreen(
//        route = "settings_screen",
//        title = "Settings",
//        icon = R.drawable.ic_settings
//    )
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AppScaffold() {
    val navController = rememberNavController()
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) {
        NavHost(
            navController = navController,
            startDestination = NavigationScreen.HomeScreen.route
        ) {
            composable(NavigationScreen.HomeScreen.route) {
                Text("Hello, world!")
            }
        }
    }
}