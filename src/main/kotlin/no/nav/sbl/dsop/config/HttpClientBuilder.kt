package no.nav.sbl.dsop.config

import io.ktor.client.HttpClient
import io.ktor.client.features.defaultRequest
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.request.header

object HttpClientBuilder {

    fun build(): HttpClient {
        return HttpClient {
            defaultRequest {
                header("Nav-Consumer-Id", CONSUMER_ID)
            }
            install(JsonFeature)
            expectSuccess = false
        }
    }

}
