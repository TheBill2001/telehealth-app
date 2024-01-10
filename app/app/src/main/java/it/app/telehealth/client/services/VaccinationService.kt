package it.app.telehealth.client.services

import it.app.telehealth.client.models.NewVaccineRegistration
import it.app.telehealth.client.models.VaccinationHistory
import it.app.telehealth.client.models.VaccinationRegistration
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface VaccinationService {
    @GET("vaccine/history")
    suspend fun getVaccineHistory(): List<VaccinationHistory>

    @GET("vaccine/registration")
    suspend fun getVaccineRegistration(): List<VaccinationRegistration>

    @POST("vaccine/registration")
    suspend fun addNewRegistration(@Body request: NewVaccineRegistration): VaccinationRegistration

    @GET("vaccine/registration/{id}")
    suspend fun getRegistrationById(@Path("id") id: String): VaccinationRegistration

    @PUT("vaccine/registration/{id}/cancel")
    suspend fun cancelRegistrationById(@Path("id") id: String): VaccinationRegistration
}