package it.app.telehealth.client

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import it.app.telehealth.client.models.LoginRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.io.IOException

data class AuthorizationStore(
    val username: String,
    val password: String
)

class AuthorizationStoreRepository(private val dataStore: DataStore<Preferences>) {

    private val TAG: String = "AuthorizationStoreRepository"

    private object PreferencesKeys {
        val USERNAME = stringPreferencesKey("username")
        val PASSWORD = stringPreferencesKey("password")
    }

    val authorizationStoreFlow: Flow<AuthorizationStore> = dataStore.data
        .catch { exception ->
            // dataStore.data throws an IOException when an error is encountered when reading data
            if (exception is IOException) {
                Log.e(TAG, "Error reading preferences.", exception)
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { preferences ->
            mapAuthorizationStore(preferences)
        }

    private fun mapAuthorizationStore(preferences: Preferences): AuthorizationStore {
        return AuthorizationStore(
            preferences[PreferencesKeys.USERNAME] ?: "",
            preferences[PreferencesKeys.PASSWORD] ?: ""
        )
    }

    suspend fun getUsernameAndPassword(): LoginRequest {
        val store = authorizationStoreFlow.first()
        return LoginRequest(store.username, store.password)
    }

    suspend fun storeUsernameAndPassword(request: LoginRequest) {
        storeUsername(request.username)
        storePassword(request.password)
    }

    suspend fun clear() {
        dataStore.edit { it.clear() }
    }

    private suspend fun storeUsername(username: String) {
        dataStore.edit { preferences -> preferences[PreferencesKeys.USERNAME] = username }
    }

    private suspend fun storePassword(password: String) {
        dataStore.edit { preferences -> preferences[PreferencesKeys.PASSWORD] = password }
    }
}