package it.app.telehealth

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import it.app.telehealth.ui.screen.LoginScreen
import it.app.telehealth.ui.theme.TeleHealthTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TeleHealthTheme {
                LoginScreen()
            }
        }
    }
}