package no.nav.dsop.config

import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.header
import io.ktor.serialization.gson.gson

object HttpClientBuilder {

    fun build(): HttpClient {
        return HttpClient {
            defaultRequest {
                header("Nav-Consumer-Id", CONSUMER_ID)
            }
            install(ContentNegotiation) {
                gson()
            }
            expectSuccess = false
        }
    }

}
