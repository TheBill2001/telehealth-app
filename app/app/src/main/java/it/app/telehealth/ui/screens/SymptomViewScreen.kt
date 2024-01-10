package it.app.telehealth.ui.screens

import android.text.format.DateFormat
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import it.app.telehealth.R
import it.app.telehealth.client.models.Symptom
import it.app.telehealth.ui.viewmodels.SymptomViewModel
import kotlinx.coroutines.launch

enum class SymptomViewScreenMode {
    View, Edit
}

enum class SymptomViewScreenState {
    Idle, Loading
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SymptomViewScreen(
    symptomViewModel: SymptomViewModel,
    navController: NavController,
    modifier: Modifier = Modifier,
    onComposing: (TopAppBarActions) -> Unit,
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var mode: SymptomViewScreenMode by remember { mutableStateOf(SymptomViewScreenMode.View) }
    var state: SymptomViewScreenState by remember { mutableStateOf(SymptomViewScreenState.Idle) }

    val id: String? by remember { mutableStateOf(symptomViewModel.selectedSymptomId) }
    var symptom: Symptom? by remember { mutableStateOf(null) }

    var description: String by remember { mutableStateOf("") }
    var severity by remember { mutableFloatStateOf(5.0f) }
    var note: String by remember { mutableStateOf("") }

    val setToSymptom = fun() {
        description = symptom?.description ?: ""
        severity = symptom?.severity ?: 5.0f
        note = symptom?.note ?: ""
    }

    val reUpdate = fun() {
        coroutineScope.launch {
            state = SymptomViewScreenState.Loading
            symptom = symptomViewModel.getUserSymptomById(id ?: "", context)
            setToSymptom()
            state = SymptomViewScreenState.Idle
        }
    }

    LaunchedEffect(id) {
        if (id != null)
            reUpdate()
        else
            mode = SymptomViewScreenMode.Edit
    }

    LaunchedEffect(true) {
        onComposing(
            TopAppBarActions {
                when (mode) {
                    SymptomViewScreenMode.Edit -> {
                        IconButton(
                            onClick = {
                                coroutineScope.launch {
                                    state = SymptomViewScreenState.Loading
                                    if (id != null) {
                                        symptomViewModel.selectedSymptomId =
                                            symptomViewModel.updateUserSymptomById(
                                                id!!,
                                                description,
                                                severity, note, context
                                            )?.id
                                        reUpdate()
                                    } else {
                                        symptomViewModel.newUserSymptom(
                                            description,
                                            severity, note, context
                                        )?.id
                                        navController.popBackStack()
                                    }
                                    state = SymptomViewScreenState.Idle
                                    mode = SymptomViewScreenMode.View
                                }
                            },
                        ) {
                            Icon(
                                imageVector = Icons.Default.Save,
                                contentDescription = stringResource(id = R.string.save)
                            )
                        }
                        IconButton(
                            onClick = {
                                setToSymptom()
                                mode = SymptomViewScreenMode.View
                            },
                        ) {
                            Icon(
                                imageVector = Icons.Default.Cancel,
                                contentDescription = stringResource(id = R.string.cancel)
                            )
                        }
                    }

                    SymptomViewScreenMode.View -> {
                        IconButton(
                            onClick = {
                                mode = SymptomViewScreenMode.Edit
                            },
                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = stringResource(id = R.string.edit)
                            )
                        }
                    }
                }
                IconButton(onClick = {
                    if (id != null)
                        symptomViewModel.deleteUserSymptoms(id!!, context)
                    navController.popBackStack()
                }) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = stringResource(id = R.string.delete)
                    )
                }
            }

        )
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
            if (mode == SymptomViewScreenMode.View) {
                val dateFormat = DateFormat.getDateFormat(context)
                val timeFormat = DateFormat.getTimeFormat(context)
                val createdAtString = try {
                    val milsec = symptom?.createdAt?.toEpochMilliseconds()
                    dateFormat.format(milsec) + " " + timeFormat.format(milsec)
                } catch (e: Exception) {
                    stringResource(R.string.unavailable)
                }

                TextField(
                    value = createdAtString,
                    onValueChange = {},
                    readOnly = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(0.dp, 0.dp, 0.dp, 10.dp),
                    label = { Text(stringResource(id = R.string.date)) }
                )
            }

            TextField(
                value = if (mode == SymptomViewScreenMode.View) symptom?.description
                    ?: stringResource(R.string.unavailable) else description,
                onValueChange = { description = it },
                enabled = state == SymptomViewScreenState.Idle,
                readOnly = mode == SymptomViewScreenMode.View,
                label = { Text(stringResource(id = R.string.description)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(0.dp, 10.dp, 0.dp, 10.dp),
            )

            Row {
                Slider(
                    value = if (mode == SymptomViewScreenMode.View) symptom?.severity
                        ?: 5.0f else severity,
                    onValueChange = { if (mode == SymptomViewScreenMode.Edit) severity = it },
                    steps = 17,
                    valueRange = 1f..10f,
                    enabled = state == SymptomViewScreenState.Idle,
                    modifier = Modifier.weight(1f)
                )

                Text(
                    text = "%.1f".format(if (mode == SymptomViewScreenMode.View) symptom?.severity else severity),
                    modifier = Modifier
                        .width(50.dp)
                        .align(Alignment.CenterVertically)
                )
            }

            TextField(
                value = if (mode == SymptomViewScreenMode.View) symptom?.note
                    ?: stringResource(R.string.unavailable) else note,
                onValueChange = { note = it },
                enabled = state == SymptomViewScreenState.Idle,
                readOnly = mode == SymptomViewScreenMode.View,
                label = { Text(stringResource(id = R.string.note)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(0.dp, 10.dp, 0.dp, 10.dp),
            )
        }
    }
}