package no.nav.sbl.dsop.consumer.sporingslogg

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
import no.nav.tms.token.support.tokendings.exchange.TokendingsServiceBuilder
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals

internal class SporingsloggConsumerTest {

    @Test
    suspend fun testSporingsloggSuccess() {
        val sporingsloggConsumer = SporingsloggConsumer(setupMockedClient(success = true), Environment(), TokendingsServiceBuilder.buildTokendingsService())

        val sporingslogg = sporingsloggConsumer.getSporingslogg(selvbetjeningstoken = "")
        assertEquals(1, sporingslogg.size)
    }

    @Test
    suspend fun testSporingsloggError() {
        val sporingsloggConsumer = SporingsloggConsumer(setupMockedClient(success = false), Environment(), TokendingsServiceBuilder.buildTokendingsService())

        assertThrows<RuntimeException> { sporingsloggConsumer.getSporingslogg(selvbetjeningstoken = "") }
    }

    private fun setupMockedClient(success: Boolean): HttpClient {
        val json = this.javaClass.getResource("/sporingslogg.json")?.readText()!!

        return HttpClient(MockEngine) {
            engine {
                addHandler { request ->
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
            install(JsonFeature)
            expectSuccess = false
        }
    }
}
