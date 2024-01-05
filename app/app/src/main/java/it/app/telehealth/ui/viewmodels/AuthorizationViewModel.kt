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
import it.app.telehealth.client.TelehealthAPI
import it.app.telehealth.client.model.LoginRequest
import it.app.telehealth.client.model.RegisterRequest
import it.app.telehealth.client.model.RegisterUserInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
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
                TelehealthAPI.authorizationService.login(request)
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
                TelehealthAPI.authorizationService.register(
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