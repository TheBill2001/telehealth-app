package it.app.telehealth.client

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import it.app.telehealth.BuildConfig
import it.app.telehealth.client.model.LoginRequest
import it.app.telehealth.client.model.RegisterRequest
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.JavaNetCookieJar
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import java.net.CookieManager
import java.util.concurrent.TimeUnit

private val cookieManager: CookieManager = CookieManager()
private val cookieJar = JavaNetCookieJar(cookieManager)
private val client: OkHttpClient = OkHttpClient.Builder()
    .cookieJar(cookieJar)
    .connectTimeout(100, TimeUnit.SECONDS)
    .readTimeout(100, TimeUnit.SECONDS).build()

@OptIn(ExperimentalSerializationApi::class)
private val retrofit = Retrofit.Builder().client(client)
    .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
    .baseUrl(BuildConfig.API_URL)
    .build()


interface AuthorizationService {
    @Headers("Accept: application/json")
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest)

    @Headers("Accept: application/json")
    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest)
}


object TelehealthAPI {
    val authorizationService: AuthorizationService by lazy {
        retrofit.create(AuthorizationService::class.java)
    }
}