package it.app.telehealth.client.models

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class VaccineRegistrationStatus {
    @SerialName("Pending")
    Pending,

    @SerialName("Canceled")
    Canceled,

    @SerialName("Accepted")
    Accepted,

    @SerialName("Finished")
    Finished,
}

@Serializable
data class VaccinationRegistration(
    @SerialName("_id")
    val id: String,
    val userId: String,
    val name: String,
    val type: VaccineType,
    val status: VaccineRegistrationStatus,
    val facility: String? = null,
    val date: Instant? = null,
    val createdAt: Instant? = null,
    val updatedAt: Instant? = null,
)
