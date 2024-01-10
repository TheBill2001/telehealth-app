package it.app.telehealth.ui.screens

import android.text.format.DateFormat
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import it.app.telehealth.R
import it.app.telehealth.ui.components.DropdownMenuButton
import it.app.telehealth.ui.components.RefreshablePage
import it.app.telehealth.ui.viewmodels.SymptomViewModel

@Composable
fun SymptomScreen(
    symptomViewModel: SymptomViewModel,
    navController: NavController,
    modifier: Modifier = Modifier,
    onComposing: (TopAppBarActions) -> Unit,
) {
    val context = LocalContext.current
    val userSymptoms by symptomViewModel.userSymptoms.observeAsState()

    LaunchedEffect(Unit) {
        symptomViewModel.fetchUserSymptoms(context)
    }

    LaunchedEffect(true) {
        onComposing(
            TopAppBarActions {
                IconButton(
                    onClick = {
                        symptomViewModel.selectedSymptomId = null
                        navController.navigate(NavigationScreen.SymptomEditScreen.route)
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = stringResource(id = R.string.add)
                    )
                }
                IconButton(
                    onClick = {
                        symptomViewModel.fetchUserSymptoms(context)
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = stringResource(id = R.string.refresh)
                    )
                }
            }
        )
    }

    RefreshablePage(
        refresh = {
            symptomViewModel.fetchUserSymptoms(context)
        },
        minArtificialDelay = 1000,
        modifier = modifier
    ) {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 300.dp),
            modifier = Modifier.padding(10.dp).fillMaxSize()
        ) {
            if (userSymptoms != null) {
                items(userSymptoms!!.size) { index ->
                    val item = userSymptoms!![index]
                    ElevatedCard(
                        modifier = Modifier
                            .padding(10.dp)
                            .fillMaxSize()
                            .clickable {
                                symptomViewModel.selectedSymptomId = item.id
                                navController.navigate(NavigationScreen.SymptomEditScreen.route)
                            }
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(10.dp)
                                .fillMaxWidth()
                        ) {
                            val color = when (item.severity) {
                                in (1.0 * Int.MIN_VALUE)..4.0 -> Color.Green
                                in 4.0..7.0 -> Color.Yellow
                                else -> Color.Red
                            }
                            val icon = when (item.severity) {
                                in (1.0 * Int.MIN_VALUE)..3.0 -> Icons.Filled.CheckCircle
                                in 4.0..7.0 -> Icons.Filled.Warning
                                else -> Icons.Filled.Error
                            }
                            Icon(
                                imageVector = icon,
                                contentDescription = "Severity",
                                tint = color,
                                modifier = Modifier
                                    .size(56.dp)
                                    .align(Alignment.CenterVertically)
                                    .padding(0.dp, 0.dp, 5.dp, 0.dp)
                            )

                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(5.dp, 0.dp, 0.dp, 0.dp)
                            ) {
                                val dateFormat = DateFormat.getDateFormat(context)
                                val timeFormat = DateFormat.getTimeFormat(context)
                                val dateString = try {
                                    val milsec = item.createdAt!!.toEpochMilliseconds()
                                    dateFormat.format(milsec) + " " + timeFormat.format(milsec)
                                } catch (e: Exception) {
                                    stringResource(R.string.unavailable)
                                }

                                Text(
                                    stringResource(R.string.symptom_date, dateString)
                                )
                                Text(
                                    stringResource(R.string.symptom_description, item.description),
                                    overflow = TextOverflow.Ellipsis,
                                    maxLines = 3
                                )
                                Text(
                                    stringResource(R.string.symptom_severity, item.severity),
                                )
                            }

                            DropdownMenuButton(
                                modifier = Modifier.align(Alignment.Top)
                            ) { close ->
                                DropdownMenuItem(
                                    text = { Text(stringResource(R.string.delete)) },
                                    onClick = {
                                        symptomViewModel.deleteUserSymptoms(item.id, context)
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