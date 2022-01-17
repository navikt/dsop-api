package no.nav.sbl.dsop.oppslag.ereg

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.mock.*
import io.ktor.client.features.json.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import no.nav.sbl.dsop.api.Environment
import no.nav.sbl.dsop.api.dto.EregOrganisasjon
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
            val result: HttpResponse = client.request("http://test.nav.no")
            client.close()
            val eregOrganisasjon = result.receive<EregOrganisasjon>()
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
        val navn = getOrganisasjonsnavn(authorization = "", orgnr = "991003525", testClient = client, environment = Environment())
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
        val navn = getOrganisasjonsnavn(authorization = "", orgnr = orgnr, testClient = client, environment = Environment())
        assertEquals(orgnr, navn)
    }
}
