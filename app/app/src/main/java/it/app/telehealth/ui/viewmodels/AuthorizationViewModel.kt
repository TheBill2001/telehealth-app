package it.app.telehealth.ui.viewmodels

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import it.app.telehealth.R
import it.app.telehealth.client.AuthorizationStoreRepository
import it.app.telehealth.client.TeleHealthAPI
import it.app.telehealth.client.models.GenericResponse
import it.app.telehealth.client.models.LoginRequest
import it.app.telehealth.client.models.RegisterRequest
import it.app.telehealth.client.models.RegisterUserInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import retrofit2.HttpException

sealed interface AuthorizationUiState {
    data object Success : AuthorizationUiState
    data object Loading : AuthorizationUiState
    data object Idle : AuthorizationUiState
}

class AuthorizationViewModel(
    private val authorizationStoreRepository: AuthorizationStoreRepository
) : ViewModel() {
    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

//    private val authorizationStoreFlow = authorizationStoreRepository.authorizationStoreFlow

    var authorizationUiState: AuthorizationUiState by mutableStateOf(AuthorizationUiState.Idle)
        private set

    fun resetUi() {
        authorizationUiState = AuthorizationUiState.Idle
    }

    fun relogin(context: Context) {
        viewModelScope.launch {
            val request = authorizationStoreRepository.getUsernameAndPassword()
            if (request.password.isNotEmpty() && request.username.isNotEmpty())
                login(request, true, context)
        }
    }

    fun login(request: LoginRequest, save: Boolean, context: Context) {
        viewModelScope.launch {
            authorizationUiState = AuthorizationUiState.Loading

            try {
                TeleHealthAPI.authorizationService.login(request)
                _isLoggedIn.value = true
                authorizationUiState = AuthorizationUiState.Success
                if (save)
                    authorizationStoreRepository.storeUsernameAndPassword(request)
            } catch (e: HttpException) {
                if (e.code() == 401) {
                    Log.i("Authorization", context.resources.getString(R.string.err_wrong_login))
                    Toast.makeText(
                        context,
                        context.resources.getString(R.string.err_wrong_login),
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    Log.i("Authorization", e.toString())
                    Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show()
                }
                authorizationUiState = AuthorizationUiState.Idle
                authorizationStoreRepository.clear()
            } catch (e: Exception) {
                Log.i("Authorization", e.toString())
                Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show()
                authorizationUiState = AuthorizationUiState.Idle
            }
        }
    }

    fun login(username: String, password: String, save: Boolean, context: Context) {
        login(LoginRequest(username, password), save, context)
    }

    fun logout(callback: () -> Unit) {
        viewModelScope.launch {
            authorizationStoreRepository.clear()
            callback()
        }
    }

    fun register(
        username: String,
        password: String,
        name: String,
        phone: String,
        email: String,
        citizenId: String,
        dateOfBirth: Long,
        context: Context
    ) {
        viewModelScope.launch {
            authorizationUiState = AuthorizationUiState.Loading
            try {
                TeleHealthAPI.authorizationService.register(
                    RegisterRequest(
                        username,
                        password,
                        RegisterUserInfo(name, phone, email, citizenId, dateOfBirth)
                    )
                )
                _isLoggedIn.value = true
                authorizationUiState = AuthorizationUiState.Success
                authorizationStoreRepository.storeUsernameAndPassword(
                    LoginRequest(
                        username,
                        password
                    )
                )
            } catch (e: HttpException) {
                if (e.code() == 409) {
                    Log.i(
                        "Authorization",
                        context.resources.getString(R.string.err_username_existed)
                    )
                    Toast.makeText(
                        context,
                        context.resources.getString(R.string.err_username_existed),
                        Toast.LENGTH_LONG
                    ).show()
                } else if (e.code() == 422) {
                    try {
                        val response = Json.decodeFromString<GenericResponse>(
                            e.response()?.errorBody()?.string() ?: ""
                        )
                        Log.i("Authorization", response.message)
                        Toast.makeText(context, response.message, Toast.LENGTH_LONG).show()
                    } catch (_: Exception) {
                        Log.i("Authorization", context.resources.getString(R.string.invalid_data))
                        Toast.makeText(
                            context,
                            context.resources.getString(R.string.invalid_data),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                } else {
                    Log.i("Authorization", e.toString())
                    Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show()
                }
                authorizationUiState = AuthorizationUiState.Idle
            } catch (e: Exception) {
                Log.i("Authorization", e.toString())
                Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show()
                authorizationUiState = AuthorizationUiState.Idle
            }
        }
    }
}

class AuthorizationViewModelFactory(
    private val authorizationStoreRepository: AuthorizationStoreRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthorizationViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AuthorizationViewModel(authorizationStoreRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}