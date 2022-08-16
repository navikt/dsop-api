package no.nav.dsop.consumer.ereg

import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.engine.mock.respondError
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.serialization.gson.gson
import kotlinx.coroutines.runBlocking
import no.nav.dsop.config.Environment
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class EregConsumerTest {

    private val eregConsumer = EregConsumer(setupMockedClient(), Environment())

    @Test
    fun testEregCallWithExistingOrgnr(): Unit = runBlocking {
        val navn = eregConsumer.getOrganisasjonsnavn(orgnr = "991003525")
        assertEquals("ARBEIDS- OG VELFERDSETATEN IKT DRIFT STEINKJER", navn)
    }

    @Test
    fun testEregCallWithNonExistingOrgnr(): Unit = runBlocking {
        val orgnr = "444555666"
        val navn = eregConsumer.getOrganisasjonsnavn(orgnr = orgnr)
        assertEquals(orgnr, navn)
    }

    private fun setupMockedClient(): HttpClient {
        val json = this.javaClass.getResource("/json/ereg-organisasjon.json")?.readText()!!

        return HttpClient(MockEngine) {
            engine {
                addHandler { request ->
                    if (request.url.encodedPath.contains("991003525")) {
                        respond(
                            json,
                            headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                        )
                    } else {
                        respondError(HttpStatusCode.NotFound)
                    }
                }
            }
            install(ContentNegotiation) {
                gson()
                expectSuccess = false
            }
        }
    }
}
