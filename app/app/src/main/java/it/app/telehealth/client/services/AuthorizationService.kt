package it.app.telehealth.client.services

import it.app.telehealth.client.models.GenericResponse
import it.app.telehealth.client.models.LoginRequest
import it.app.telehealth.client.models.RegisterRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface AuthorizationService {
    @Headers("Accept: application/json")
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest)

    @Headers("Accept: application/json")
    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest) : GenericResponse?
}