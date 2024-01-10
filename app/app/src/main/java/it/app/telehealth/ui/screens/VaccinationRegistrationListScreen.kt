package it.app.telehealth.ui.screens

import android.text.format.DateFormat
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import it.app.telehealth.R
import it.app.telehealth.client.models.VaccinationRegistration
import it.app.telehealth.client.models.VaccineRegistrationStatus
import it.app.telehealth.ui.components.DropdownMenuButton
import it.app.telehealth.ui.components.RefreshablePage
import it.app.telehealth.ui.viewmodels.VaccinationRegistrationListViewModel

@Composable
fun VaccinationRegistrationListScreen(
    navigationController: NavController,
    vaccinationRegistrationListViewModel: VaccinationRegistrationListViewModel,
    modifier: Modifier = Modifier,
    onComposing: (TopAppBarActions) -> Unit = {},
) {
    val context = LocalContext.current
    val registrationList: List<VaccinationRegistration> by vaccinationRegistrationListViewModel.vaccinationRegistrationList.collectAsState()

    LaunchedEffect(true) {
        onComposing(
            TopAppBarActions {
                IconButton(
                    onClick = {
                        navigationController.navigate(NavigationScreen.VaccinationRegistrationAddScreen.route)
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = stringResource(id = R.string.add)
                    )
                }

                IconButton(
                    onClick = {
                        vaccinationRegistrationListViewModel.fetchVaccinationRegistrationList(
                            context
                        )
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = stringResource(id = R.string.refresh)
                    )
                }
            }
        )
        vaccinationRegistrationListViewModel.fetchVaccinationRegistrationList(context)
    }

    RefreshablePage(refresh = {
        vaccinationRegistrationListViewModel.fetchVaccinationRegistrationList(
            context
        )
    }, modifier = modifier, minArtificialDelay = 1000) {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 300.dp),
            modifier = Modifier
                .padding(10.dp)
                .fillMaxSize()
        ) {
            items(registrationList) { item ->
                val cardColor = when (item.status) {
                    VaccineRegistrationStatus.Pending -> MaterialTheme.colorScheme.surfaceContainer
                    VaccineRegistrationStatus.Canceled -> MaterialTheme.colorScheme.errorContainer
                    VaccineRegistrationStatus.Accepted -> MaterialTheme.colorScheme.primaryContainer
                    VaccineRegistrationStatus.Finished -> MaterialTheme.colorScheme.secondaryContainer
                }

                ElevatedCard(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(0.dp, 10.dp),
                    colors = CardDefaults.elevatedCardColors(
                        containerColor = cardColor,
                        contentColor = MaterialTheme.colorScheme.contentColorFor(cardColor)
                    )
                ) {
                    Row(
                        Modifier.fillMaxWidth().padding(20.dp)
                    ) {
                        Column(
                            Modifier.fillMaxWidth().weight(1f)
                        ) {
                            Text(
                                stringResource(R.string.vaccine_name, item.name)
                            )

                            Text(
                                stringResource(R.string.facility, item.facility)
                            )

                            Text(
                                stringResource(R.string.vaccine_type, item.type.toString())
                            )

                            Text(
                                stringResource(
                                    R.string.vaccine_regis_status,
                                    item.status.toString()
                                )
                            )

                            if (item.date != null) {
                                val dateFormat = DateFormat.getDateFormat(context)
                                val timeFormat = DateFormat.getTimeFormat(context)
                                val dateString = try {
                                    val milsec = item.date.toEpochMilliseconds()
                                    dateFormat.format(milsec) + " " + timeFormat.format(milsec)
                                } catch (e: Exception) {
                                    stringResource(R.string.unavailable)
                                }

                                Text(
                                    stringResource(R.string.symptom_date, dateString)
                                )
                            }
                        }

                        if (item.status == VaccineRegistrationStatus.Pending) {
                            DropdownMenuButton(
                                modifier = Modifier.align(Alignment.Top)
                            ) { close ->
                                DropdownMenuItem(
                                    text = { Text(stringResource(R.string.cancel)) },
                                    onClick = {
                                        vaccinationRegistrationListViewModel.cancelVaccinationRegistration(
                                            item.id,
                                            context
                                        )
                                        close()
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}