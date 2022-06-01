package no.nav.sbl.dsop.consumer.ereg

import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.engine.mock.respondError
import io.ktor.client.features.json.JsonFeature
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import no.nav.sbl.dsop.config.Environment
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import kotlin.test.assertEquals

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class EregConsumerTest {

    @Test
    fun testEregCallWithExistingOrgnr() {
        val eregConsumer = EregConsumer(setupMockedClient(), Environment())

        val navn = eregConsumer.getOrganisasjonsnavn(selvbetjeningstoken = "", authorization = "", orgnr = "991003525")
        assertEquals("ARBEIDS- OG VELFERDSETATEN IKT DRIFT STEINKJER", navn)
    }

    @Test
    fun testEregCallWithNonExistingOrgnr() {
        val orgnr = "444555666"

        val eregConsumer = EregConsumer(setupMockedClient(), Environment())

        val navn = eregConsumer.getOrganisasjonsnavn(selvbetjeningstoken = "", authorization = "", orgnr = orgnr)
        assertEquals(orgnr, navn)
    }

    private fun setupMockedClient(): HttpClient {
        val json = this.javaClass.getResource("/ereg-organisasjon-991003525.json")?.readText()!!

        return HttpClient(MockEngine) {
            engine {
                addHandler { request ->
                    if (request.url.encodedPath.contains("991003525")) {
                        respond(json, headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString()))
                    } else {
                        respondError(HttpStatusCode.NotFound)
                    }
                }
            }
            install(JsonFeature)
            expectSuccess = false
        }
    }
}
