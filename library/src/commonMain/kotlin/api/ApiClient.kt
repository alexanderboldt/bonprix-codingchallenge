package org.challenge.api

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

/**
 * Manages the connection to the backend and contains the individual routes.
 */
internal class ApiClient {

    private val BASE_URL = "https://assets.www.bonprix.com/"
    private val WARDROBE = "codingchallenge/wardrobe.json"

    private val client = HttpClient(CIO) {
        expectSuccess = true

        install(ContentNegotiation) {
            json(
                Json {
                    prettyPrint = true
                    isLenient = false
                    ignoreUnknownKeys = true
                }
            )
        }

        defaultRequest {
            url(BASE_URL)
        }
    }

    suspend fun getAllGarments(): HttpResponse = client.get(WARDROBE)
}