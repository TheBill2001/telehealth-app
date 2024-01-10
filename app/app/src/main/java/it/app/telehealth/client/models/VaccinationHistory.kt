package it.app.telehealth.client.models

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class VaccineType {
    @SerialName("Vaccine")
    Vaccine,
    @SerialName("Booster")
    Booster
}

@Serializable
data class VaccinationHistory(
    @SerialName("_id")
    val id: String,
    val userId: String,
    val name: String,
    val type: VaccineType,
    val facility: String,
    val date: Instant,
)
