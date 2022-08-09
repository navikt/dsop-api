package no.nav.dsop.consumer.sporingslogg

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
import io.mockk.coEvery
import no.nav.dsop.config.Environment
import no.nav.tms.token.support.tokendings.exchange.TokendingsServiceBuilder
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

internal class SporingsloggConsumerTest {

    val tokendingsService = TokendingsServiceBuilder.buildTokendingsService()

    @Test
    suspend fun testSporingsloggSuccess() {
        coEvery { tokendingsService.exchangeToken(any(), any()) } returns ""

        val sporingsloggConsumer =
            SporingsloggConsumer(setupMockedClient(success = true), Environment(), tokendingsService)

        val sporingslogg = sporingsloggConsumer.getSporingslogg(selvbetjeningstoken = "")
        assertEquals(1, sporingslogg.size)
    }

    @Test
    suspend fun testSporingsloggError() {
        coEvery { tokendingsService.exchangeToken(any(), any()) } returns ""

        val sporingsloggConsumer =
            SporingsloggConsumer(setupMockedClient(success = false), Environment(), tokendingsService)

        assertThrows<RuntimeException> { sporingsloggConsumer.getSporingslogg(selvbetjeningstoken = "") }
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
