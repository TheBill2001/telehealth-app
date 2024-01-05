package it.app.telehealth

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import it.app.telehealth.client.AuthorizationStoreRepository
import it.app.telehealth.ui.TeleHealthApp
import it.app.telehealth.ui.theme.TeleHealthTheme
import it.app.telehealth.ui.viewmodels.AuthorizationViewModel
import it.app.telehealth.ui.viewmodels.AuthorizationViewModelFactory

private val Context.dataStore by preferencesDataStore(
    name = "authorization_store",
)

class MainActivity : ComponentActivity() {

    private lateinit var authorizationViewModel: AuthorizationViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        authorizationViewModel = ViewModelProvider(
            this,
            AuthorizationViewModelFactory(
                AuthorizationStoreRepository(dataStore)
            )
        )[AuthorizationViewModel::class.java]

        setContent {
            TeleHealthTheme {
                TeleHealthApp(authorizationViewModel)
            }
        }
    }
}
