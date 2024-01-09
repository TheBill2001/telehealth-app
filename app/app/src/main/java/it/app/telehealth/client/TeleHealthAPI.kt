package it.app.telehealth.client

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import it.app.telehealth.BuildConfig
import it.app.telehealth.client.services.AuthorizationService
import it.app.telehealth.client.services.ProfileService
import it.app.telehealth.client.services.SymptomService
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.JavaNetCookieJar
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import java.net.CookieManager
import java.util.concurrent.TimeUnit

private val cookieManager: CookieManager = CookieManager()
private val cookieJar = JavaNetCookieJar(cookieManager)
private val client: OkHttpClient = OkHttpClient.Builder()
    .cookieJar(cookieJar)
    .connectTimeout(100, TimeUnit.SECONDS)
    .readTimeout(100, TimeUnit.SECONDS).build()

private val json = Json{ ignoreUnknownKeys = true }

@OptIn(ExperimentalSerializationApi::class)
private val retrofit = Retrofit.Builder().client(client)
    .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
    .baseUrl(BuildConfig.API_URL)
    .build()

object TeleHealthAPI {
    val authorizationService: AuthorizationService by lazy {
        retrofit.create(AuthorizationService::class.java)
    }

    val profileService: ProfileService by lazy {
        retrofit.create(ProfileService::class.java)
    }

    val symptomService: SymptomService by lazy {
        retrofit.create(SymptomService::class.java)
    }
}