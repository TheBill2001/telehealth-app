package it.app.telehealth.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material3.CardDefaults.elevatedCardColors
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import it.app.telehealth.R
import it.app.telehealth.ui.viewmodels.AuthorizationViewModel
import it.app.telehealth.ui.viewmodels.ProfileViewModel

@Composable
fun HomeScreen(
    profileViewModel: ProfileViewModel,
    authorizationViewModel: AuthorizationViewModel,
    navController: NavController,
    modifier: Modifier = Modifier,
    logout: () -> Unit,
    onComposing: (TopAppBarActions) -> Unit = {},
) {
    val context = LocalContext.current
    val currentUserProfile by profileViewModel.currentUserProfile.observeAsState()
    val isLoggedInState by authorizationViewModel.isLoggedIn.collectAsState()

    val greetingString: String by remember {
        derivedStateOf {
            if (currentUserProfile?.name != null) {
                try {
                    context.resources.getString(
                        R.string.greeting_string_1,
                        currentUserProfile?.name?.split(" ")?.last() ?: ""
                    )
                } catch (e: NoSuchElementException) {
                    context.resources.getString(R.string.greeting_string_2)
                }
            } else
                context.resources.getString(R.string.greeting_string_2)
        }
    }

    LaunchedEffect(Unit) {
        if (isLoggedInState && currentUserProfile == null) {
            profileViewModel.fetchCurrentUserProfile(context)
        }
    }

    LaunchedEffect(true) {
        onComposing(
            TopAppBarActions {
                IconButton(
                    onClick = {
                        authorizationViewModel.logout {
                            logout()
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Logout,
                        contentDescription = stringResource(id = R.string.add)
                    )
                }
            }
        )
    }

    Surface(
        modifier = modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ElevatedCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(20.dp),
                colors = elevatedCardColors(
                    MaterialTheme.colorScheme.secondaryContainer,
                    MaterialTheme.colorScheme.contentColorFor(MaterialTheme.colorScheme.secondaryContainer)
                )
            ) {
                Text(
                    text = greetingString,
                    fontSize = 58.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp, 20.dp, 20.dp, 10.dp)
                )
            }

            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 150.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp, 10.dp, 20.dp, 20.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp),
                horizontalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                items(
                    listOf(
                        NavigationScreen.SymptomScreen,
                        NavigationScreen.CovidTestResultScreen,
                        NavigationScreen.VaccinationHistory,
                        NavigationScreen.ProfileScreen,
                    )
                ) { screen ->
                    ElevatedCard(
                        modifier = Modifier.clickable {
                            navController.navigate(screen.route)
                        }
                    ) {
                        Column(
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .padding(20.dp)
                        ) {
                            val density = LocalDensity.current.density

                            Icon(
                                imageVector = screen.icon,
                                contentDescription = stringResource(screen.title),
                                modifier = Modifier
                                    .size(56.dp)
                                    .align(Alignment.CenterHorizontally)
                            )

                            Spacer(modifier = Modifier.height(4.dp))
                            var padding by remember { mutableStateOf(0.dp) }
                            Text(
                                text = stringResource(screen.title),
                                modifier = Modifier
                                    .align(Alignment.CenterHorizontally)
                                    .padding(0.dp, 10.dp, 0.dp, padding),
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis,
                                textAlign = TextAlign.Center,
                                onTextLayout = {
                                    val lineCount = it.lineCount
                                    val height = (it.size.height / density).dp
                                    padding = if (lineCount > 1) 0.dp else height
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}