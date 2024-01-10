package it.app.telehealth.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.filled.Coronavirus
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import it.app.telehealth.R
import it.app.telehealth.ui.viewmodels.AuthorizationViewModel
import it.app.telehealth.ui.viewmodels.CovidTestResultViewModel
import it.app.telehealth.ui.viewmodels.ProfileViewModel
import it.app.telehealth.ui.viewmodels.SymptomViewModel
import it.app.telehealth.ui.viewmodels.VaccinationHistoryViewModel

sealed class NavigationScreen(
    val route: String,
    val title: Int,
    val icon: ImageVector,
) {
    data object HomeScreen : NavigationScreen(
        route = "home_screen",
        title = R.string.home_screen,
        icon = Icons.Filled.Home
    )

    data object SymptomScreen : NavigationScreen(
        route = "symptom_screen",
        title = R.string.symptom_screen,
        icon = Icons.Filled.Coronavirus,
    )

    data object SymptomEditScreen : NavigationScreen(
        route = "symptom_edit_screen",
        title = R.string.symptom_edit_screen,
        icon = Icons.Filled.Edit,
    )

    data object CovidTestResultScreen : NavigationScreen(
        route = "covid_test_result_screen",
        title = R.string.covid_test_results_screen,
        icon = Icons.Default.Science
    )

    data object VaccinationHistory : NavigationScreen (
        route = "vaccination_history_screen",
        title = R.string.vaccination_history_screen,
        icon = Icons.Default.Verified
    )

    data object ProfileScreen : NavigationScreen(
        route = "Profile_screen",
        title = R.string.profile_screen,
        icon = Icons.Filled.Person
    )
}

data class TopAppBarActions(
    val actions: (@Composable RowScope.() -> Unit)? = null
)

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AppScaffold(
    authorizationViewModel: AuthorizationViewModel,
    logout: () -> Unit
) {
    val navController = rememberNavController()
    val snackbarHostState = remember { SnackbarHostState() }
    var currentScreen: NavigationScreen by remember { mutableStateOf(NavigationScreen.HomeScreen) }
    var topAppBarActions: TopAppBarActions by remember { mutableStateOf(TopAppBarActions()) }

    val symptomViewModel: SymptomViewModel = viewModel()
    val profileViewModel: ProfileViewModel = viewModel()
    val covidTestResultViewModel: CovidTestResultViewModel = viewModel()
    val vaccinationHistoryViewModel: VaccinationHistoryViewModel = viewModel()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(currentScreen.title),
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    if (currentScreen != NavigationScreen.HomeScreen) {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                                contentDescription = "Localized description"
                            )
                        }
                    }
                },
                actions = {
                    topAppBarActions.actions?.invoke(this)
                },
                colors = TopAppBarColors(
                    MaterialTheme.colorScheme.secondaryContainer,
                    MaterialTheme.colorScheme.secondaryContainer,
                    MaterialTheme.colorScheme.contentColorFor(MaterialTheme.colorScheme.secondaryContainer),
                    MaterialTheme.colorScheme.contentColorFor(MaterialTheme.colorScheme.secondaryContainer),
                    MaterialTheme.colorScheme.contentColorFor(MaterialTheme.colorScheme.secondaryContainer)
                )
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = NavigationScreen.HomeScreen.route
        ) {
            composable(NavigationScreen.HomeScreen.route) {
                HomeScreen(
                    profileViewModel,
                    authorizationViewModel,
                    navController,
                    modifier = Modifier.padding(innerPadding),
                    logout = {
                        logout()
                    }
                ) {
                    topAppBarActions = it
                }
                currentScreen = NavigationScreen.HomeScreen
            }
            composable(NavigationScreen.SymptomScreen.route) {
                SymptomScreen(
                    symptomViewModel,
                    navController = navController,
                    modifier = Modifier.padding(innerPadding)
                ) {
                    topAppBarActions = it
                }
                currentScreen = NavigationScreen.SymptomScreen
            }
            composable(NavigationScreen.SymptomEditScreen.route) {
                SymptomViewScreen(
                    symptomViewModel = symptomViewModel,
                    modifier = Modifier.padding(innerPadding),
                    navController = navController
                ) {
                    topAppBarActions = it
                }
                currentScreen = NavigationScreen.SymptomEditScreen
            }
            composable(NavigationScreen.CovidTestResultScreen.route) {
                CovidTestResultScreen(
                    covidTestResultViewModel = covidTestResultViewModel,
                    modifier = Modifier.padding(innerPadding),
                ) {
                    topAppBarActions = it
                }
                currentScreen = NavigationScreen.CovidTestResultScreen
            }
            composable(NavigationScreen.VaccinationHistory.route) {
                VaccinationHistoryScreen(
                    vaccinationHistoryViewModel = vaccinationHistoryViewModel,
                    modifier = Modifier.padding(innerPadding)
                ) {
                    topAppBarActions = it
                }
                currentScreen = NavigationScreen.VaccinationHistory
            }
            composable(NavigationScreen.ProfileScreen.route) {
                ProfileScreen(
                    profileViewModel,
                    authorizationViewModel,
                    modifier = Modifier.padding(innerPadding)
                ) {
                    logout()
                }
                topAppBarActions = TopAppBarActions {}
                currentScreen = NavigationScreen.ProfileScreen
            }
        }
    }
}

@Preview
@Composable
fun AppScaffoldPreview() {
    AppScaffold(viewModel()) {}
}