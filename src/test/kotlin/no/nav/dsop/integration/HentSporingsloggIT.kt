package no.nav.dsop.integration

import io.kotest.assertions.json.shouldEqualJson
import io.kotest.matchers.shouldBe
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import no.nav.dsop.config.setupMockedClient
import no.nav.dsop.testutils.readJsonFile
import org.junit.jupiter.api.Test

class HentSporingsloggIT : IntegrationTest() {

    val HENT_SPORINGSLOGG_PATH = "/get"

    @Test
    fun hentSporingslogg200() {
        integrationTest(setupMockedClient()) {
            val response = get(HENT_SPORINGSLOGG_PATH)

            response.status shouldBe HttpStatusCode.OK
            response.bodyAsText() shouldEqualJson readJsonFile("/json/expected-response/sporingslogg.json")
        }
    }

    @Test
    fun feilMotEregSkalGi200() {
        integrationTest(setupMockedClient(eregStatus = HttpStatusCode.InternalServerError)) {
            val response = get(HENT_SPORINGSLOGG_PATH)

            response.status shouldBe HttpStatusCode.OK
            response.bodyAsText() shouldEqualJson readJsonFile("/json/expected-response/sporingslogg-with-ereg-error.json")
        }
    }

    @Test
    fun feilMotKodeverkSkalGi200() {
        integrationTest(setupMockedClient(kodeverkStatus = HttpStatusCode.InternalServerError)) {
            val response = get(HENT_SPORINGSLOGG_PATH)

            response.status shouldBe HttpStatusCode.OK
            response.bodyAsText() shouldEqualJson readJsonFile("/json/expected-response/sporingslogg-with-kodeverk-error.json")
        }
    }

    @Test
    fun feilMotSporingsloggSkalGi500() {
        integrationTest(setupMockedClient(sporingsloggStatus = HttpStatusCode.InternalServerError)) {
            val response = get(HENT_SPORINGSLOGG_PATH)

            response.status shouldBe HttpStatusCode.InternalServerError
        }
    }
}