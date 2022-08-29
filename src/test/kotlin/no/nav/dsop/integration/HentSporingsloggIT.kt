package no.nav.dsop.integration

import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import no.nav.dsop.config.mock.setupMockedClient
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class HentSporingsloggIT : IntegrationTest() {

    val HENT_SPORINGSLOGG_PATH = "/get"

    @Test
    fun hentSporingslogg200() = integrationTest(setupMockedClient()) {
        val client = createClient { install(ContentNegotiation) { json() } }

        val response = get(client, HENT_SPORINGSLOGG_PATH)

        assertEquals(HttpStatusCode.OK, response.status)
    }

    @Test
    fun feilMotEregSkalGi200() =
        integrationTest(setupMockedClient(eregStatus = HttpStatusCode.InternalServerError)) {
            val client = createClient { install(ContentNegotiation) { json() } }

            val response = get(client, HENT_SPORINGSLOGG_PATH)

            assertEquals(HttpStatusCode.OK, response.status)
        }

    @Test
    fun feilMotSporingsloggSkalGi500() =
        integrationTest(setupMockedClient(sporingsloggStatus = HttpStatusCode.InternalServerError)) {
            val client = createClient { install(ContentNegotiation) { json() } }

            val response = get(client, HENT_SPORINGSLOGG_PATH)

            assertEquals(HttpStatusCode.InternalServerError, response.status)
        }

    @Test
    fun feilMotKodeverkSkalGi200() =
        integrationTest(setupMockedClient(kodeverkStatus = HttpStatusCode.InternalServerError)) {
            val client = createClient { install(ContentNegotiation) { json() } }

            val response = get(client, HENT_SPORINGSLOGG_PATH)

            assertEquals(HttpStatusCode.OK, response.status)
        }
}