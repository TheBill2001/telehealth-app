package it.app.telehealth.ui.screens

import android.text.format.DateFormat
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import it.app.telehealth.R
import it.app.telehealth.client.models.VaccinationHistory
import it.app.telehealth.ui.viewmodels.VaccinationHistoryViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VaccinationHistoryScreen(
    vaccinationHistoryViewModel: VaccinationHistoryViewModel,
    modifier: Modifier = Modifier,
    onComposing: (TopAppBarActions) -> Unit,
) {
    val context = LocalContext.current
    val history: List<VaccinationHistory> by vaccinationHistoryViewModel.vaccinationHistory.collectAsState()

    LaunchedEffect(true) {
        onComposing(
            TopAppBarActions {
                IconButton(
                    onClick = {
                        vaccinationHistoryViewModel.fetchVaccinationHistory(context)
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = stringResource(id = R.string.refresh)
                    )
                }
            }
        )
        vaccinationHistoryViewModel.fetchVaccinationHistory(context)
    }

    Surface(
        modifier = modifier
            .fillMaxSize()
            .imePadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
        ) {
            ElevatedCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(10.dp),
                colors = CardDefaults.elevatedCardColors(
                    MaterialTheme.colorScheme.secondaryContainer,
                    MaterialTheme.colorScheme.contentColorFor(MaterialTheme.colorScheme.secondaryContainer)
                )
            ) {
                Row(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(10.dp)
                ) {
                    Icon(
                        imageVector = when (history.count()) {
                            in Int.MIN_VALUE..0 -> Icons.Default.Error
                            else -> Icons.Default.CheckCircle
                        },
                        tint = when (history.count()) {
                            in Int.MIN_VALUE..0 -> Color.Red
                            1 -> Color.Yellow
                            else -> Color.Green
                        },
                        contentDescription = null,
                        modifier = Modifier
                            .size(96.dp)
                            .align(Alignment.CenterVertically)
                    )
                    Text(
                        text = stringResource(if (history.isNotEmpty()) R.string.vaccine_congrat else R.string.vaccine_shame),
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .padding(20.dp, 0.dp, 0.dp, 0.dp),
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis,
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp,
                        lineHeight = 32.sp
                    )
                }
            }

            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 300.dp),
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxSize()
            ) {
                items(history) { item ->
                    ElevatedCard(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(0.dp, 10.dp)
                    ) {
                        Column(
                            Modifier
                                .fillMaxWidth()
                                .padding(20.dp)
                        ) {
                            val dateFormat = DateFormat.getDateFormat(context)
                            val timeFormat = DateFormat.getTimeFormat(context)
                            val dateString = try {
                                val milsec = item.date.toEpochMilliseconds()
                                dateFormat.format(milsec) + " " + timeFormat.format(milsec)
                            } catch (e: Exception) {
                                stringResource(R.string.unavailable)
                            }

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
                                stringResource(R.string.symptom_date, dateString)
                            )
                        }
                    }
                }
            }
        }
    }
}