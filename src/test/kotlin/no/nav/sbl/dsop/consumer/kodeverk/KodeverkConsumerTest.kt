package no.nav.sbl.dsop.consumer.kodeverk

import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.features.json.JsonFeature
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.headersOf
import no.nav.sbl.dsop.config.Environment
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import kotlin.test.assertEquals

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class KodeverkConsumerTest {

    @Test
    fun testOppslagPaaTema() {
        val kodeverkConsumer = KodeverkConsumer(setupMockedClient(), Environment())

        val navn = kodeverkConsumer.getKodeverk("AAP")
        assertEquals("Arbeidsavklaringspenger", navn)
    }

    @Test
    fun testOppslagPaaTemaIkkeFunnet() {
        val kodeverkConsumer = KodeverkConsumer(setupMockedClient(), Environment())

        val navn = kodeverkConsumer.getKodeverk("XYZ")
        assertEquals("XYZ", navn)
    }

    private fun setupMockedClient(): HttpClient {
        val json = this.javaClass.getResource("/kodeverk-tema.json")?.readText()!!

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
