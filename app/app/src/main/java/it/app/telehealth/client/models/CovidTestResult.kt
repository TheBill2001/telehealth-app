package it.app.telehealth.client.models

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CovidTestResult(
    @SerialName("_id")
    val id: String,
    val userId: String,
    val positive: Boolean,
    val testingFacility: String? = null,
    val type: Int,
    val createdAt: Instant? = null,
    val updatedAt: Instant? = null
)

@Serializable
data class NewCovidTestResult(
    val positive: Boolean
)