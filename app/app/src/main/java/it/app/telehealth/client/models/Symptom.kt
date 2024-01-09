package it.app.telehealth.client.models

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Symptom(
    @SerialName("_id")
    val id: String,
    var description: String,
    var severity: Float,
    var note: String? = null,
    val createdAt: Instant? = null,
    val updatedAt: Instant? = null
)

@Serializable
data class SymptomRequest(
    val description: String,
    val severity: Float,
    val note: String? = null,
)

@Serializable
data class UserSymptom(
    val userId: String,
    val symptoms: List<Symptom>? = listOf()
)