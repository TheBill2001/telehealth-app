package it.app.telehealth.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import it.app.telehealth.ui.screens.AppScaffold
import it.app.telehealth.ui.screens.LoginScreen
import it.app.telehealth.ui.screens.RegisterScreen
import it.app.telehealth.ui.viewmodels.AuthorizationViewModel

sealed class Screen(val route: String) {
    data object LoginScreen : Screen("login_screen")
    data object RegisterScreen : Screen("register_screen")
    data object AppScaffold : Screen("app_scaffold")
}

@Composable
fun TeleHealthApp(
    authorizationViewModel: AuthorizationViewModel
) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.LoginScreen.route) {
        composable(
            route = Screen.LoginScreen.route,
            content = {
                LoginScreen(
                    navController = navController,
                    authorizationViewModel = authorizationViewModel
                )
            }
        )
        composable(
            route = Screen.RegisterScreen.route,
            content = {
                RegisterScreen(
                    navController = navController,
                    authorizationViewModel = authorizationViewModel
                )
            }
        )
        composable(
            route = Screen.AppScaffold.route,
            content = {
                AppScaffold(authorizationViewModel) {
                    navController.navigate(Screen.LoginScreen.route)
                }
            }
        )
    }
}