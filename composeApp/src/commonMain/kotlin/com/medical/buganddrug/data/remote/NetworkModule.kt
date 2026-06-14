package com.medical.buganddrug.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.header
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.dsl.module
import io.ktor.client.plugins.logging.*

val networkModule = module {

    single { SharedPreferenceManager(get()) }

    single {
        val preferenceManager: SharedPreferenceManager = get()

        HttpClient {
            install(Logging) {
                logger = Logger.SIMPLE
                level = LogLevel.ALL   // BODY, HEADERS, INFO, ALL
            }
            defaultRequest {
                url("http://154.26.128.29:905/api/")

                preferenceManager.getToken()?.let { token ->
                    header("Authorization", "Bearer $token")
                }
            }

            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    isLenient = true

                    // ⭐ KEY SETTINGS
                    explicitNulls = false
                    coerceInputValues = true

                })
            }
        }
    }

    single {
        ApiService(get())
    }
}