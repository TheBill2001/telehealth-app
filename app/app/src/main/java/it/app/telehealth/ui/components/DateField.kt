package it.app.telehealth.ui.components

import android.text.format.DateFormat
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import it.app.telehealth.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateField(
    state: DatePickerState,
    modifier: Modifier = Modifier,
    label: String = "",
    placeholder: String = "",
    enabled: Boolean = true
) {
    val openDialog = remember { mutableStateOf(false) }
    val dateFormat = DateFormat.getDateFormat(LocalContext.current)
    val dateString: String by remember {
        derivedStateOf { dateFormat.format(state.selectedDateMillis ?: 0) }
    }

    TextField(
        value = dateString,
        onValueChange = { },
        label = { Text(label) },
        placeholder = { Text(placeholder) },
        singleLine = true,
        modifier = modifier,
        readOnly = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        trailingIcon = {
            IconButton(onClick = { openDialog.value = true }) {
                Icon(
                    imageVector = Icons.Filled.CalendarMonth,
                    contentDescription = stringResource(R.string.open_date_picket)
                )
            }
        },
        enabled = enabled
    )

    if (openDialog.value) {
        DatePickerDialog(
            onDismissRequest = {
                openDialog.value = false
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        openDialog.value = false
                    }
                ) {
                    Text(stringResource(R.string.accept))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        openDialog.value = false
                    }
                ) {
                    Text(stringResource(R.string.cancel))
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(0.dp, 10.dp),
        ) {
            DatePicker(state = state)
        }
    }
}