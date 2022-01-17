package no.nav.sbl.dsop.oppslag.kodeverk

import io.ktor.client.*
import io.ktor.client.engine.mock.*
import io.ktor.client.features.json.*
import io.ktor.http.*
import no.nav.sbl.dsop.api.Environment
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import java.io.InputStreamReader
import kotlin.test.assertEquals

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class KodeverkCallTest {
    @Test
    fun testOppslagPaaTema() {
        val json = InputStreamReader(this.javaClass.getResourceAsStream("/kodeverk-tema.json")!!).readText()

        val client1 = createTestClient(json)
        val client2 = createTestClient(json)

        var navn = getKodeverk(authorization = "", kode = "AAP", testClient = client1, environment = Environment())
        assertEquals("Arbeidsavklaringspenger", navn)
        navn = getKodeverk(authorization = "", kode = "XYZ", testClient = client2, environment = Environment())
        assertEquals("XYZ", navn)
    }

    @Test
    fun testFeilResponsFraKodeverk() {
        val client = HttpClient(MockEngine) {
            engine {
                addHandler { request ->
                    respond(
                        "",
                        HttpStatusCode.NotFound,
                        headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                    )
                }
            }
            install(JsonFeature)
            expectSuccess = false
        }
        val navn = getKodeverk(authorization = "", kode = "AAP", testClient = client, environment = Environment())
        assertEquals("AAP", navn)
    }

    private fun createTestClient(json: String): HttpClient {
        return HttpClient(MockEngine) {
            engine {
                addHandler {
                    respond(json, headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString()))
                }
            }
            install(JsonFeature)
        }
    }
}
