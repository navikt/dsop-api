package no.nav.dsop.config.mock

import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respondError
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import no.nav.dsop.config.jsonConfig

fun setupMockedClient(
    eregStatus: HttpStatusCode = HttpStatusCode.OK,
    kodeverkStatus: HttpStatusCode = HttpStatusCode.OK,
    sporingsloggStatus: HttpStatusCode = HttpStatusCode.OK
): HttpClient {
    val EREG = "ereg"
    val KODEVERK = "kodeverk"
    val SPORINGSLOGG = "sporingslogg"

    return HttpClient(MockEngine) {
        engine {
            addHandler { request ->
                when (request.url.host) {
                    EREG -> {
                        mockEreg(eregStatus)
                    }
                    KODEVERK -> {
                        mockKodeverk(kodeverkStatus)
                    }
                    SPORINGSLOGG -> {
                        mockSporingslogg(sporingsloggStatus)
                    }
                    else -> {
                        respondError(HttpStatusCode.NotFound)
                    }
                }
            }

        }
        install(ContentNegotiation) {
            json(jsonConfig())
        }
        install(HttpTimeout)
        expectSuccess = false
    }
}

fun readJson(name: String): String {
    return object {}.javaClass.getResource(name)?.readText()!!
}