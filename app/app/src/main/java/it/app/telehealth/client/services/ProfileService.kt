package it.app.telehealth.client.services

import it.app.telehealth.client.models.UserProfile
import retrofit2.http.GET

interface ProfileService {
    @GET("profile")
    suspend fun getCurrentUserProfile() : UserProfile
}