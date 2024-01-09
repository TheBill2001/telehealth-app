package it.app.telehealth.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
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
import it.app.telehealth.ui.viewmodels.AuthorizationViewModel
import it.app.telehealth.ui.viewmodels.ProfileViewModel

@Composable
fun ProfileScreen(
    profileViewModel: ProfileViewModel,
    authorizationViewModel: AuthorizationViewModel,
    modifier: Modifier = Modifier,
    logout: () -> Unit
) {
    val currentUserProfile by profileViewModel.currentUserProfile.observeAsState()
    val context = LocalContext.current

    Surface(
        modifier = modifier
            .fillMaxSize()
            .imePadding()
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ElevatedCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Text(
                    text = stringResource(R.string.user_profile),
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                )

                TextField(
                    value = valueOrUnavailable(currentUserProfile?.name, context),
                    onValueChange = {},
                    readOnly = true,
                    label = { Text(stringResource(R.string.your_name)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp, 10.dp)
                )

                TextField(
                    value = valueOrUnavailable(currentUserProfile?.dateOfBirth, context),
                    onValueChange = {},
                    readOnly = true,
                    label = { Text(stringResource(R.string.date_of_birth)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp, 10.dp)
                )

                TextField(
                    value = valueOrUnavailable(currentUserProfile?.phone, context),
                    onValueChange = {},
                    readOnly = true,
                    label = { Text(stringResource(R.string.phone_number)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp, 10.dp)
                )

                TextField(
                    value = valueOrUnavailable(currentUserProfile?.email, context),
                    onValueChange = {},
                    readOnly = true,
                    label = { Text(stringResource(R.string.email)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp, 10.dp)
                )

                TextField(
                    value = valueOrUnavailable(currentUserProfile?.citizenID, context),
                    onValueChange = {},
                    readOnly = true,
                    label = { Text(stringResource(R.string.citizen_id)) },
                    modifier = Modifier
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
                modifier = Modifier.wrapContentWidth()
            ) {
                Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = stringResource(R.string.logout))
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