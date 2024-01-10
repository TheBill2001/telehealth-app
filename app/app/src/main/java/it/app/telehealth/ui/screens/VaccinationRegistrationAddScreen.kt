package it.app.telehealth.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import it.app.telehealth.R
import it.app.telehealth.client.models.VaccineType
import it.app.telehealth.ui.viewmodels.VaccinationRegistrationListViewModel
import kotlinx.coroutines.launch
import kotlinx.serialization.json.JsonNull.content

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VaccinationRegistrationAddScreen(
    navController: NavController,
    vaccinationRegistrationListViewModel: VaccinationRegistrationListViewModel,
    modifier: Modifier = Modifier,
    onComposing: (TopAppBarActions) -> Unit = {},
) {
    val context = LocalContext.current

    var vaccineName: String by remember { mutableStateOf("") }
    var type: VaccineType by remember { mutableStateOf(VaccineType.Vaccine) }
    var facility: String by remember { mutableStateOf("") }

    LaunchedEffect(true) {
        onComposing(
            TopAppBarActions {
                IconButton(
                    onClick = {
                        vaccinationRegistrationListViewModel.addVaccinationRegistration(vaccineName, type, facility, context)
                        navController.popBackStack()
                    },
                ) {
                    Icon(
                        imageVector = Icons.Default.Save,
                        contentDescription = stringResource(id = R.string.save)
                    )
                }
                IconButton(
                    onClick = {
                        navController.popBackStack()
                    },
                ) {
                    Icon(
                        imageVector = Icons.Default.Cancel,
                        contentDescription = stringResource(id = R.string.cancel)
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
            TextField(
                value = vaccineName,
                onValueChange = { vaccineName = it },
                label = { Text(stringResource(id = R.string.vaccine_label)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(0.dp, 10.dp, 0.dp, 10.dp),
            )

            var expanded by remember { mutableStateOf(false) }
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(0.dp, 10.dp, 0.dp, 10.dp),
            ) {
                TextField(
                    readOnly = true,
                    value = type.toString(),
                    onValueChange = { },
                    label = { Text(stringResource(id = R.string.vaccine_type_label)) },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(
                            expanded = expanded
                        )
                    },
                    colors = ExposedDropdownMenuDefaults.textFieldColors(),
                    modifier = Modifier.menuAnchor().fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = {
                        expanded = false
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    DropdownMenuItem(
                        text = { Text(text = VaccineType.Vaccine.toString()) },
                        onClick = {
                            type = VaccineType.Vaccine
                            expanded = false
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                    DropdownMenuItem(
                        text = { Text(text = VaccineType.Booster.toString()) },
                        onClick = {
                            type = VaccineType.Booster
                            expanded = false
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            TextField(
                value = facility,
                onValueChange = { facility = it },
                label = { Text(stringResource(id = R.string.facility_label)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(0.dp, 10.dp, 0.dp, 10.dp),
            )
        }
    }
}