package it.app.telehealth.client.model

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    val username: String,
    val password: String
)

@Serializable
data class RegisterUserInfo(
    val name: String,
    val phone: String,
    val email: String,
    val citizenID: String,
    val dateOfBirth: Long
)

@Serializable
data class RegisterRequest(
    val username: String,
    val password: String,
    val userInfo: RegisterUserInfo
)
