package it.app.telehealth.ui.screens

import android.content.Context
import android.text.format.DateFormat
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import it.app.telehealth.R
import it.app.telehealth.client.models.UserProfile
import it.app.telehealth.ui.viewmodels.AuthorizationViewModel
import it.app.telehealth.ui.viewmodels.ProfileViewModel
import kotlinx.datetime.Instant
import java.text.SimpleDateFormat

@Composable
fun ProfileScreen(
    profileViewModel: ProfileViewModel,
    authorizationViewModel: AuthorizationViewModel,
    modifier: Modifier = Modifier,
    logout: () -> Unit
) {
    val isLoggedInState by authorizationViewModel.isLoggedIn.collectAsState()
    val currentUserProfile by profileViewModel.currentUserProfile.observeAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        if (isLoggedInState && currentUserProfile == null) {
            profileViewModel.fetchCurrentUserProfile(context)
        }
    }

    Surface(
        modifier = modifier
            .fillMaxSize()
            .imePadding()
    ) {
        Column(
            modifier = modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ElevatedCard(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Text(
                    text = stringResource(R.string.user_profile),
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                )

                TextField(
                    value = valueOrUnavailable(currentUserProfile?.name, context),
                    onValueChange = {},
                    readOnly = true,
                    label = { Text(stringResource(R.string.your_name)) },
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(20.dp, 10.dp)
                )

                TextField(
                    value = valueOrUnavailable(currentUserProfile?.dateOfBirth, context),
                    onValueChange = {},
                    readOnly = true,
                    label = { Text(stringResource(R.string.date_of_birth)) },
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(20.dp, 10.dp)
                )

                TextField(
                    value = valueOrUnavailable(currentUserProfile?.phone, context),
                    onValueChange = {},
                    readOnly = true,
                    label = { Text(stringResource(R.string.phone_number)) },
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(20.dp, 10.dp)
                )

                TextField(
                    value = valueOrUnavailable(currentUserProfile?.email, context),
                    onValueChange = {},
                    readOnly = true,
                    label = { Text(stringResource(R.string.email)) },
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(20.dp, 10.dp)
                )

                TextField(
                    value = valueOrUnavailable(currentUserProfile?.citizenID, context),
                    onValueChange = {},
                    readOnly = true,
                    label = { Text(stringResource(R.string.citizen_id)) },
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(20.dp, 10.dp, 20.dp, 20.dp)
                )
            }

            Button(
                onClick = {
                    authorizationViewModel.logout {
                        logout()
                    }
                },
                modifier = modifier.wrapContentWidth()
            ) {
                Icon(Icons.Filled.Logout, contentDescription = stringResource(R.string.logout))
                Text(stringResource(R.string.logout))
            }
        }
    }
}

@Preview
@Composable
fun ProfileScreenPreview() {
    ProfileScreen(viewModel(), viewModel()) {}
}

/**
 * Return the value as string. Return "Unavailable" if null.
 */
fun valueOrUnavailable(value: String?, context: Context): String {
    if (value == null)
        return context.resources.getString(R.string.unavailable)
    return value
}

/**
 * Return the [Instant] value as string. Return "Unavailable" if null.
 */
fun valueOrUnavailable(value: Instant?, context: Context): String {
    if (value == null)
        return context.resources.getString(R.string.unavailable)

    val dateFormat = DateFormat.getDateFormat(context)
    return dateFormat.format(value.toEpochMilliseconds())
}