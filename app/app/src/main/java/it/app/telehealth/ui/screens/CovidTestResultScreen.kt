package it.app.telehealth.ui.screens

import android.text.format.DateFormat
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import it.app.telehealth.R
import it.app.telehealth.client.models.CovidTestResult
import it.app.telehealth.ui.components.RefreshablePage
import it.app.telehealth.ui.viewmodels.CovidTestResultViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun CovidTestResultScreen(
    covidTestResultViewModel: CovidTestResultViewModel,
    modifier: Modifier = Modifier,
    onComposing: (TopAppBarActions) -> Unit,
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val openAddDialog = remember { mutableStateOf(false) }
    val testResults: List<CovidTestResult> by covidTestResultViewModel.testResults.collectAsState()


    LaunchedEffect(true) {
        onComposing(
            TopAppBarActions {
                IconButton(
                    onClick = {
                        openAddDialog.value = true
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = stringResource(id = R.string.add)
                    )
                }

                IconButton(
                    onClick = {
                        covidTestResultViewModel.fetchUserTestResult(context)
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = stringResource(id = R.string.refresh)
                    )
                }
            }
        )
        covidTestResultViewModel.fetchUserTestResult(context)
    }

    if (openAddDialog.value) {
        Dialog(onDismissRequest = {openAddDialog.value = false}) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(375.dp)
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    val checked = remember { mutableStateOf(false) }
                    
                    Text(
                        text = stringResource(R.string.add_test),
                        modifier = Modifier.padding(16.dp),
                        fontWeight = FontWeight.Bold
                    )
                
                    Row (modifier = Modifier.padding(16.dp)) {
                        Text(text = stringResource(id = R.string.add_test_question), modifier = Modifier.align(Alignment.CenterVertically))
                        Checkbox(checked = checked.value, onCheckedChange =  {checked.value = it}, modifier = Modifier.align(Alignment.CenterVertically))
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                    ) {
                        TextButton(
                            onClick = { openAddDialog.value = false },
                            modifier = Modifier.padding(8.dp),
                        ) {
                            Text(stringResource(id = R.string.cancel))
                        }
                        TextButton(
                            onClick = {
                                covidTestResultViewModel.addNewTestResult(checked.value, context)
                                openAddDialog.value = false
                                coroutineScope.launch {
                                    delay(100)
                                    covidTestResultViewModel.fetchUserTestResult(context)
                                }
                            },
                            modifier = Modifier.padding(8.dp),
                        ) {
                            Text(stringResource(id = R.string.add))
                        }
                    }

                }
            }
        }
    }

    RefreshablePage(
        refresh = {
            covidTestResultViewModel.fetchUserTestResult(context)
        },
        minArtificialDelay = 1000,
        modifier = modifier
    ) {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 300.dp),
            modifier = Modifier
                .padding(10.dp)
                .fillMaxSize()
        ) {
            items(testResults) {
                ElevatedCard(
                    modifier = Modifier
                        .padding(10.dp)
                        .fillMaxSize()
                ) {
                    Row(
                        modifier = Modifier
                            .padding(10.dp)
                            .fillMaxWidth()
                    ) {
                        val color = if (!it.positive) Color.Green else Color.Red

                        val icon = if (!it.positive) Icons.Filled.CheckCircle
                        else Icons.Filled.Error

                        Icon(
                            imageVector = icon,
                            contentDescription = "Positive",
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
                                val milsec = it.createdAt!!.toEpochMilliseconds()
                                dateFormat.format(milsec) + " " + timeFormat.format(milsec)
                            } catch (e: Exception) {
                                stringResource(R.string.unavailable)
                            }

                            Text(
                                stringResource(R.string.symptom_date, dateString)
                            )

                            Text(
                                stringResource(
                                    R.string.test_type,
                                    stringResource(if (it.type == 0) R.string.test_type_self else R.string.test_type_facility)
                                )
                            )

                            if (it.type == 1 && it.testingFacility != null) {
                                Text(
                                    stringResource(R.string.facility, it.testingFacility),
                                    overflow = TextOverflow.Ellipsis,
                                    maxLines = 2
                                )
                            }

                            Text(
                                stringResource(
                                    R.string.positive,
                                    stringResource(if (it.positive) R.string.yes else R.string.no)
                                ),
                            )
                        }
                    }
                }
            }
        }
    }
}