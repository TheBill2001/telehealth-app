package it.app.telehealth.client.models

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class UserProfile(
    val id: String,
    val name: String,
    val phone: String,
    val email: String,
    val dateOfBirth: Instant,
    val citizenID: String
)
