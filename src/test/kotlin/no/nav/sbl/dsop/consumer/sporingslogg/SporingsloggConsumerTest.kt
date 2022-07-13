package no.nav.sbl.dsop.consumer.sporingslogg

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
import no.nav.sbl.dsop.config.Environment
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

internal class SporingsloggConsumerTest {

    @Test
    fun testSporingsloggSuccess() {
        val sporingsloggConsumer = SporingsloggConsumer(setupMockedClient(success = true), Environment())

        val sporingslogg = sporingsloggConsumer.getSporingslogg(selvbetjeningstoken = "", authorization = "")
        assertEquals(1, sporingslogg.size)
    }

    @Test
    fun testSporingsloggError() {
        val sporingsloggConsumer = SporingsloggConsumer(setupMockedClient(success = false), Environment())

        assertThrows<RuntimeException> {
            sporingsloggConsumer.getSporingslogg(
                selvbetjeningstoken = "",
                authorization = ""
            )
        }
    }

    private fun setupMockedClient(success: Boolean): HttpClient {
        val json = this.javaClass.getResource("/sporingslogg.json")?.readText()!!

        return HttpClient(MockEngine) {
            engine {
                addHandler {
                    if (success) {
                        respond(
                            json,
                            headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                        )
                    } else {
                        respondError(HttpStatusCode.InternalServerError)
                    }
                }
            }
            install(ContentNegotiation) {
                gson()
            }
            expectSuccess = false
        }
    }
}