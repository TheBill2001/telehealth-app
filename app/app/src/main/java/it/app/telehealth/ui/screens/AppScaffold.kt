package it.app.telehealth.ui.screens

import android.annotation.SuppressLint
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import it.app.telehealth.ui.viewmodels.AuthorizationViewModel
import it.app.telehealth.ui.viewmodels.ProfileViewModel

sealed class NavigationScreen(val route: String, val title: String, val icon: ImageVector) {
    data object HomeScreen : NavigationScreen(
        route = "home_screen",
        title = "Home",
        icon = Icons.Filled.Home
    )

    data object ProfileScreen : NavigationScreen(
        route = "Profile_screen",
        title = "Profile",
        icon = Icons.Filled.Person
    )
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AppScaffold(
    authorizationViewModel: AuthorizationViewModel,
    logout : ()->Unit
) {
    val navController = rememberNavController()
    val snackbarHostState = remember { SnackbarHostState() }
    var selectedItem by remember { mutableIntStateOf(0) }
    val items = listOf(
        NavigationScreen.HomeScreen,
        NavigationScreen.ProfileScreen
    )

    val profileViewModel: ProfileViewModel = viewModel()

    navController.addOnDestinationChangedListener { _, navigationDestination, _ ->
        val index = items.indexOf(items.find{screen -> screen.route == navigationDestination.route })
        selectedItem = if (index > -1) index else 0
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            NavigationBar {
                items.forEachIndexed { index, item ->
                    NavigationBarItem(
                        icon = { Icon(item.icon, item.title) },
                        onClick = {
                            selectedItem = index
                            navController.navigate(item.route) {
                                popUpTo(navController.graph.startDestinationId)
                                launchSingleTop = true
                            }
                        },
                        label = { Text(item.title) },
                        selected = selectedItem == index,
                    )
                }
            }
        }
    ) {
        NavHost(
            navController = navController,
            startDestination = NavigationScreen.HomeScreen.route
        ) {
            composable(NavigationScreen.HomeScreen.route) {
                HomeScreen(profileViewModel, authorizationViewModel, navController = navController)
            }
            composable(NavigationScreen.ProfileScreen.route) {
                ProfileScreen(profileViewModel, authorizationViewModel) {
                    logout()
                }
            }
        }
    }
}

@Preview
@Composable
fun AppScaffoldPreview() {
    AppScaffold(viewModel()) {}
}