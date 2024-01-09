package it.app.telehealth.client.services

import it.app.telehealth.client.models.Symptom
import it.app.telehealth.client.models.SymptomRequest
import it.app.telehealth.client.models.UserSymptom
import kotlinx.datetime.Instant
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface SymptomService {
    @GET("symptom")
    suspend fun getUserSymptoms(
        @Query("from") from: Instant? = null,
        @Query("to") to: Instant? = null,
        @Query("desk") desc: Boolean? = null
    ): UserSymptom

    @POST("symptom")
    @Headers("Accept: application/json")
    suspend fun addUserSymptom(@Body request: SymptomRequest): Symptom

    @GET("symptom/{id}")
    suspend fun getUserSymptomById(@Path("id") id: String): Symptom

    @PUT("symptom/{id}")
    @Headers("Accept: application/json")
    suspend fun updateSymptomById(@Path("id") id: String, @Body request: SymptomRequest): Symptom

    @DELETE("symptom/{id}")
    suspend fun deleteSymptomById(@Path("id") id: String)
}