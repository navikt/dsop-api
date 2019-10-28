package no.nav.sbl.dsop.oppslag.kodeverk

import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.features.json.JsonFeature
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import no.nav.sbl.dsop.api.Environment
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import java.io.InputStreamReader
import kotlin.test.assertEquals

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class KodeverkCallTest {
    @Test
    fun testOppslagPaaTema() {
        val json = InputStreamReader(this.javaClass.getResourceAsStream("/kodeverk-tema.json")).readText()

        val client = HttpClient(MockEngine) {
            engine {
                addHandler { request ->
                    respond(json, headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString()))
                }
            }
            install(JsonFeature)
        }
        var navn = getKodeverk(authorization = "", kode = "AAP", testClient = client, environment = Environment())
        assertEquals("Arbeidsavklaringspenger", navn)
        navn = getKodeverk(authorization = "", kode = "XYZ", testClient = client, environment = Environment())
        assertEquals("XYZ", navn)
    }

    @Test
    fun testFeilResponsFraKodeverk() {
        val client = HttpClient(MockEngine) {
            engine {
                addHandler { request ->
                    respond("",
                            HttpStatusCode.NotFound,
                            headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString()))
                    }
            }
            install(JsonFeature)
            expectSuccess = false
        }
        val navn = getKodeverk(authorization = "", kode = "AAP", testClient = client, environment = Environment())
        assertEquals("AAP", navn)
    }
}
