package no.nav.sbl.dsop.consumer.kodeverk

import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.headersOf
import io.ktor.serialization.gson.gson
import no.nav.sbl.dsop.config.Environment
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class KodeverkConsumerTest {

    private val kodeverkConsumer = KodeverkConsumer(setupMockedClient(), Environment())

    @Test
    suspend fun testOppslagPaaTema() {
        val navn = kodeverkConsumer.getKodeverk("AAP")
        assertEquals("Arbeidsavklaringspenger", navn)
    }

    @Test
    suspend fun testOppslagPaaTemaIkkeFunnet() {
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
            install(ContentNegotiation) {
                gson()
                expectSuccess = false
            }
        }
    }
}
