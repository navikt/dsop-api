package no.nav.dsop.config

import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respondError
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import no.nav.dsop.config.mocks.mockEreg
import no.nav.dsop.config.mocks.mockKodeverk
import no.nav.dsop.config.mocks.mockSporingslogg

private const val EREG = "ereg"
private const val KODEVERK = "kodeverk"
private const val SPORINGSLOGG = "sporingslogg"

fun setupMockedClient(
    eregStatus: HttpStatusCode = HttpStatusCode.OK,
    kodeverkStatus: HttpStatusCode = HttpStatusCode.OK,
    sporingsloggStatus: HttpStatusCode = HttpStatusCode.OK
): HttpClient {
    return HttpClient(MockEngine) {
        engine {
            addHandler { request ->
                when (request.url.host) {
                    EREG -> mockEreg(eregStatus)
                    KODEVERK -> mockKodeverk(kodeverkStatus)
                    SPORINGSLOGG -> mockSporingslogg(sporingsloggStatus)
                    else -> respondError(HttpStatusCode.NotFound)
                }
            }

        }
        install(ContentNegotiation) {
            json(jsonConfig())
        }
        install(HttpTimeout)
    }
}