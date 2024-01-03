package it.app.telehealth.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import it.app.telehealth.R

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxSize(),
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
                value = "",
                onValueChange = {},
                label = { Text(stringResource(R.string.username_input)) },
                placeholder = { Text(stringResource(R.string.username_input)) },
                singleLine = true,
                modifier = modifier.padding(0.dp, 10.dp)
            )

            // Reference: https://stackoverflow.com/a/66998457
            var passwordVisible by rememberSaveable { mutableStateOf(false) }
            TextField(
                value = "",
                onValueChange = {},
                label = { Text(stringResource(R.string.password_input)) },
                placeholder = { Text(stringResource(R.string.password_input)) },
                singleLine = true,
                modifier = modifier.padding(0.dp, 10.dp),
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    val image = if (passwordVisible)
                        Icons.Filled.Visibility
                    else Icons.Filled.VisibilityOff

                    // Please provide localized description for accessibility services
                    val description = if (passwordVisible) "Hide password" else "Show password"

                    IconButton(onClick = {passwordVisible = !passwordVisible}){
                        Icon(imageVector  = image, description)
                    }
                }
            )

            Button(
                onClick = { /*TODO*/ },
                modifier = modifier.width(150.dp).padding(0.dp, 50.dp, 0.dp, 20.dp)
            ) {
                Text(stringResource(R.string.login_button), fontSize = 18.sp)
            }

            Button(
                onClick = { /*TODO*/ },
                modifier = modifier.width(150.dp)
            ) {
                Text(stringResource(R.string.register_button), fontSize = 18.sp)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    LoginScreen()
}