package it.app.telehealth.ui.screens

import android.util.Patterns
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import it.app.telehealth.R
import it.app.telehealth.ui.Screen
import it.app.telehealth.ui.components.DateField
import it.app.telehealth.ui.components.PasswordTextField
import it.app.telehealth.ui.viewmodels.AuthorizationUiState
import it.app.telehealth.ui.viewmodels.AuthorizationViewModel
import java.time.Instant

@Composable
fun LoginScreen(
    navController: NavController,
    authorizationViewModel: AuthorizationViewModel,
    modifier: Modifier = Modifier,
) {
    if (authorizationViewModel.authorizationUiState == AuthorizationUiState.Success) {
        navController.popBackStack()
        navController.navigate(Screen.AppScaffold.route) {
            popUpTo(navController.graph.startDestinationId)
            launchSingleTop = true
        }
        authorizationViewModel.resetUi()
    }

    val context = LocalContext.current

    LaunchedEffect(Unit) {
        authorizationViewModel.relogin(context)
    }

    var username: String by remember { mutableStateOf("") }
    var password: String by remember { mutableStateOf("") }
    var saveLogin: Boolean by remember { mutableStateOf(false) }

    Surface(
        modifier = modifier
            .fillMaxSize()
            .imePadding(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(R.string.app_name),
                fontSize = 48.sp,
                fontWeight = FontWeight.Bold,
                modifier = modifier.padding(0.dp, 50.dp)
            )

            TextField(
                value = username,
                onValueChange = { username = it },
                label = { Text(stringResource(R.string.username_input)) },
                placeholder = { Text(stringResource(R.string.username_input)) },
                singleLine = true,
                modifier = modifier.padding(0.dp, 10.dp),
                enabled = authorizationViewModel.authorizationUiState == AuthorizationUiState.Idle,
            )

            PasswordTextField(
                value = password,
                onValueChange = { password = it },
                modifier = modifier.padding(0.dp, 10.dp),
                label = stringResource(R.string.password_input),
                placeholder = stringResource(R.string.password_input)
            )

            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(0.dp, 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Checkbox(checked = saveLogin, onCheckedChange = { saveLogin = it })
                Text(stringResource(R.string.save_login))
            }


            Button(
                onClick = {
                    authorizationViewModel.login(username, password, saveLogin, context)
                },
                modifier = modifier
                    .width(150.dp)
                    .padding(0.dp, 50.dp, 0.dp, 20.dp),
                enabled = authorizationViewModel.authorizationUiState == AuthorizationUiState.Idle
            ) {
                Text(stringResource(R.string.login_button), fontSize = 18.sp)
            }

            Button(
                onClick = {
                    navController.navigate(Screen.RegisterScreen.route) {
                        popUpTo(navController.graph.startDestinationId)
                        launchSingleTop = true
                    }
                },
                modifier = modifier.width(150.dp),
                enabled = authorizationViewModel.authorizationUiState == AuthorizationUiState.Idle
            ) {
                Text(stringResource(R.string.register_button), fontSize = 18.sp)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    navController: NavController,
    authorizationViewModel: AuthorizationViewModel,
    modifier: Modifier = Modifier
) {
    if (authorizationViewModel.authorizationUiState == AuthorizationUiState.Success) {
        navController.popBackStack()
        navController.navigate(Screen.AppScaffold.route) {
            popUpTo(navController.graph.startDestinationId)
            launchSingleTop = true
        }
        authorizationViewModel.resetUi()
    }

    val context = LocalContext.current
    var username: String by remember { mutableStateOf("") }
    var password: String by remember { mutableStateOf("") }
    var passwordCheck: String by remember { mutableStateOf("") }
    var name: String by remember { mutableStateOf("") }
    var phone: String by remember { mutableStateOf("") }
    var email: String by remember { mutableStateOf("") }
    var citizenId: String by remember { mutableStateOf("") }
    val dateOfBirth = rememberDatePickerState(
        initialSelectedDateMillis = Instant.now().toEpochMilli(),
    )

    val usernameOk: Boolean by remember {
        derivedStateOf { usernameValidation(username) }
    }
    val passwordOk: Boolean by remember {
        derivedStateOf { passwordValidation(password) }
    }
    val phoneOk: Boolean by remember {
        derivedStateOf { phoneValidation(phone) }
    }
    val emailOk: Boolean by remember {
        derivedStateOf { emailValidation(email) }
    }
    val citizenIdOk: Boolean by remember {
        derivedStateOf { citizenIDValidation(citizenId) }
    }
    val dateOk: Boolean by remember {
        derivedStateOf { dateValidation(dateOfBirth.selectedDateMillis ?: 0) }
    }

    Surface(
        modifier = modifier
            .fillMaxSize()
            .imePadding(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = modifier
                .verticalScroll(rememberScrollState())
                .fillMaxSize()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Text(
                text = stringResource(R.string.register_an_account),
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                modifier = modifier.padding(0.dp, 0.dp, 0.dp, 20.dp)
            )

            TextField(
                value = username,
                onValueChange = { username = it },
                label = { Text(text = stringResource(R.string.username_input)) },
                placeholder = { Text(text = stringResource(R.string.username_input)) },
                singleLine = true,
                modifier = modifier
                    .fillMaxWidth()
                    .padding(0.dp, 10.dp),
                enabled = authorizationViewModel.authorizationUiState == AuthorizationUiState.Idle,
            )

            Text(
                stringResource(R.string.username_requirement),
                modifier
                    .fillMaxWidth()
                    .padding(0.dp, 10.dp),
                color = if (username.isNotEmpty() && !usernameOk) Color.Red else Color.Unspecified
            )

            PasswordTextField(
                value = password,
                onValueChange = { password = it },
                modifier = modifier
                    .fillMaxWidth()
                    .padding(0.dp, 10.dp),
                label = stringResource(R.string.password_input),
                placeholder = stringResource(R.string.password_input)
            )

            Text(
                stringResource(R.string.password_requirement),
                modifier = modifier
                    .fillMaxWidth()
                    .padding(0.dp, 10.dp, 0.dp, 0.dp)
            )
            Text(
                stringResource(R.string.password_requirement_1),
                modifier = modifier
                    .fillMaxWidth(),
                color = if (passwordValidation1(password)) Color.Unspecified else Color.Red
            )
            Text(
                stringResource(R.string.password_requirement_2),
                modifier = modifier
                    .fillMaxWidth(),
                color = if (passwordValidation2(password)) Color.Unspecified else Color.Red
            )
            Text(
                stringResource(R.string.password_requirement_3),
                modifier = modifier
                    .fillMaxWidth(),
                color = if (passwordValidation3(password)) Color.Unspecified else Color.Red
            )
            Text(
                stringResource(R.string.password_requirement_4),
                modifier = modifier
                    .fillMaxWidth(),
                color = if (passwordValidation4(password)) Color.Unspecified else Color.Red
            )
            Text(
                stringResource(R.string.password_requirement_5),
                modifier = modifier
                    .fillMaxWidth()
                    .padding(0.dp, 0.dp, 0.dp, 10.dp),
                color = if (passwordValidation5(password)) Color.Unspecified else Color.Red
            )

            PasswordTextField(
                value = passwordCheck,
                onValueChange = { passwordCheck = it },
                modifier = modifier
                    .fillMaxWidth()
                    .padding(0.dp, 10.dp),
                label = stringResource(R.string.reenter_password),
                placeholder = stringResource(R.string.reenter_password)
            )

            if ((password.isNotEmpty() || passwordCheck.isNotEmpty()) && password != passwordCheck) {
                Text(
                    stringResource(R.string.password_do_not_match),
                    modifier
                        .fillMaxWidth()
                        .padding(0.dp, 10.dp),
                    color = Color.Red
                )
            }

            TextField(
                value = name,
                onValueChange = { name = it },
                label = { Text(stringResource(R.string.your_name)) },
                placeholder = { Text(stringResource(R.string.your_name)) },
                singleLine = true,
                modifier = modifier
                    .fillMaxWidth()
                    .padding(0.dp, 10.dp),
                enabled = authorizationViewModel.authorizationUiState == AuthorizationUiState.Idle,
            )

            TextField(
                value = phone,
                onValueChange = { phone = it },
                label = { Text(stringResource(R.string.phone_number)) },
                placeholder = { Text(stringResource(R.string.phone_number)) },
                singleLine = true,
                modifier = modifier
                    .fillMaxWidth()
                    .padding(0.dp, 10.dp),
                enabled = authorizationViewModel.authorizationUiState == AuthorizationUiState.Idle,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            if (phone.isNotEmpty() && !phoneOk) {
                Text(
                    stringResource(R.string.invalid_phone),
                    modifier
                        .fillMaxWidth()
                        .padding(0.dp, 10.dp),
                    color = Color.Red
                )
            }

            TextField(
                value = email,
                onValueChange = { email = it },
                label = { Text(stringResource(R.string.email)) },
                placeholder = { Text(stringResource(R.string.email)) },
                singleLine = true,
                modifier = modifier
                    .fillMaxWidth()
                    .padding(0.dp, 10.dp),
                enabled = authorizationViewModel.authorizationUiState == AuthorizationUiState.Idle,
            )

            if (email.isNotEmpty() && !emailOk) {
                Text(
                    stringResource(R.string.invalid_email),
                    modifier
                        .fillMaxWidth()
                        .padding(0.dp, 10.dp),
                    color = Color.Red
                )
            }

            TextField(
                value = citizenId,
                onValueChange = { citizenId = it },
                label = { Text(stringResource(R.string.citizen_id)) },
                placeholder = { Text(stringResource(R.string.citizen_id)) },
                singleLine = true,
                modifier = modifier
                    .fillMaxWidth()
                    .padding(0.dp, 10.dp),
                enabled = authorizationViewModel.authorizationUiState == AuthorizationUiState.Idle,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            if (citizenId.isNotEmpty() && !citizenIdOk) {
                Text(
                    stringResource(R.string.invalid_citizen_id),
                    modifier
                        .fillMaxWidth()
                        .padding(0.dp, 10.dp),
                    color = Color.Red
                )
            }

            DateField(
                state = dateOfBirth,
                modifier = modifier
                    .fillMaxWidth()
                    .padding(0.dp, 10.dp),
                label = stringResource(R.string.date_of_birth),
                placeholder = stringResource(R.string.date_of_birth),
                enabled = authorizationViewModel.authorizationUiState == AuthorizationUiState.Idle,
            )

            if (!dateOk) {
                Text(
                    stringResource(R.string.invalid_b_date),
                    modifier
                        .fillMaxWidth()
                        .padding(0.dp, 10.dp),
                    color = Color.Red
                )
            }

            Button(
                onClick = {
                    authorizationViewModel.register(
                        username,
                        password,
                        name,
                        phone,
                        email,
                        citizenId,
                        dateOfBirth.selectedDateMillis ?: 0,
                        context
                    )
                },
                modifier = Modifier.align(Alignment.End),
                enabled = usernameOk && passwordOk && phoneOk && emailOk && citizenIdOk && name.isNotEmpty() && dateOk && authorizationViewModel.authorizationUiState == AuthorizationUiState.Idle
            ) {
                Text(
                    stringResource(R.string.register_button)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    LoginScreen(navController = rememberNavController(), authorizationViewModel = viewModel())
}

@Preview
@Composable
fun RegisterScreenPreview() {
    RegisterScreen(navController = rememberNavController(), authorizationViewModel = viewModel())
}

fun usernameValidation(u: String): Boolean {
    return u.length in 8..32
}

/**
 * At least one digit [0-9]
 * At least one lowercase character [a-z]
 * At least one uppercase character [A-Z]
 * At least one special character [*.!@#$%^&(){}[]:;<>,?/~_+-=|\]
 * At least 8 characters in length, but no more than 32.
 * ^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[*.!@$%^&(){}\[\]:;<>,?/~_+-=|\\]).{8,32}$
 * Regex is a pain.
 */
fun passwordValidation(p: String): Boolean {
    return p.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[*.!@\$%^&(){}\\[\\]:;<>,?/~_+\\-=|\\\\]).{8,32}\$".toRegex()) // No touchy
}

fun passwordValidation1(p: String): Boolean {
    return p.contains("[0-9]".toRegex())
}

fun passwordValidation2(p: String): Boolean {
    return p.contains("[a-z]".toRegex())
}

fun passwordValidation3(p: String): Boolean {
    return p.contains("[A-Z]".toRegex())
}

fun passwordValidation4(p: String): Boolean {
    return p.contains("[*.!@\$%^&(){}\\[\\]:;<>,?/~_+\\-=|\\\\]".toRegex())
}

fun passwordValidation5(p: String): Boolean {
    return p.length in 8..32
}

fun phoneValidation(p: String): Boolean {
    return Patterns.PHONE.matcher(p).matches()
}

fun emailValidation(e: String): Boolean {
    return Patterns.EMAIL_ADDRESS.matcher(e).matches()
}

fun citizenIDValidation(c: String): Boolean {
    return c.matches("^[0-9]{12}$".toRegex())
}

fun dateValidation(date: Long): Boolean {
    return date <= Instant.now().toEpochMilli()
}