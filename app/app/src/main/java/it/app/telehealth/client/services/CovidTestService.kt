package it.app.telehealth.client.services

import it.app.telehealth.client.models.CovidTestResult
import it.app.telehealth.client.models.NewCovidTestResult
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST

interface CovidTestService {
    @GET("covidTest")
    suspend fun getUserCovidTests(): List<CovidTestResult>

    @POST("covidTest")
    @Headers("Accept: application/json")
    suspend fun addNewTestResult(@Body request: NewCovidTestResult)
}