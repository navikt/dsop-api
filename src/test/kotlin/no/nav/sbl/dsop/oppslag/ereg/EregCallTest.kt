package no.nav.sbl.dsop.oppslag.ereg

import io.ktor.client.HttpClient
import io.ktor.client.call.call
import io.ktor.client.call.receive
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.features.json.JsonFeature
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import kotlinx.coroutines.runBlocking
import no.nav.sbl.dsop.api.dto.EregOrganisasjon
import no.nav.sbl.dsop.oppslag.emptyTestEnvironment
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import java.io.InputStreamReader
import kotlin.test.assertEquals

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class EregCallTest {
    @Test
    fun testJsonDeserialization() {
        val json = InputStreamReader(this.javaClass.getResourceAsStream("/ereg-organisasjon-991003525.json")).readText()

        val client = HttpClient(MockEngine) {
            engine {
                addHandler { request ->
                    respond(json, headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString()))
                }
            }
            install(JsonFeature)
        }
        runBlocking {
            val result = client.call("http://test.nav.no")
            client.close()
            val eregOrganisasjon = result.response.receive<EregOrganisasjon>()
            assertEquals("ARBEIDS- OG VELFERDSETATEN", eregOrganisasjon.navn.navnelinje1)
            assertEquals("ARBEIDS- OG VELFERDSETATEN IKT DRIFT STEINKJER", eregOrganisasjon.navn.getNavn())
        }
    }

    @Test
    fun testEregCallWithExistingOrgnr() {
        val json = InputStreamReader(this.javaClass.getResourceAsStream("/ereg-organisasjon-991003525.json")).readText()

        val client = HttpClient(MockEngine) {
            engine {
                addHandler { request ->
                    if (request.url.encodedPath.contains("991003525")) {
                        respond(json, headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString()))
                    } else {
                        error("Fant ikke organisasjon")
                    }
                }
            }
            install(JsonFeature)
        }
        val navn = getOrganisasjonsnavn(authorization = "", orgnr = "991003525", testClient = client, environment = emptyTestEnvironment)
        assertEquals("ARBEIDS- OG VELFERDSETATEN IKT DRIFT STEINKJER", navn)
    }

    @Test
    fun testEregCallWithNonExistingOrgnr() {
        val orgnr = "444555666"
        val client = HttpClient(MockEngine) {
            engine {
                addHandler { request ->
                    respond("{\n" +
                            "  \"melding\": \"Ingen organisasjon med organisasjonsnummer $orgnr ble funnet\"\n" +
                            "}",
                            HttpStatusCode.NotFound,
                            headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString()))
                }
            }
            install(JsonFeature)
            expectSuccess = false
        }
        val navn = getOrganisasjonsnavn(authorization = "", orgnr = orgnr, testClient = client, environment = emptyTestEnvironment)
        assertEquals(orgnr, navn)
    }
}