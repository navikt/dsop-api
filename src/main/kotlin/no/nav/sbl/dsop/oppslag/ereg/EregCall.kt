package no.nav.sbl.dsop.oppslag.ereg

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import mu.KotlinLogging
import no.nav.common.log.MDCConstants
import no.nav.sbl.dsop.api.CONSUMER_ID
import no.nav.sbl.dsop.api.Environment
import no.nav.sbl.dsop.api.dto.EregOrganisasjon
import org.slf4j.MDC

private val logger = KotlinLogging.logger {}

fun getOrganisasjonsnavn(
    authorization: String,
    selvbetjeningstoken: String,
    orgnr: String,
    testClient: HttpClient? = null,
    environment: Environment,
): String = runBlocking {
    val eregClient = testClient ?: HttpClient {
        defaultRequest {
            header("Authorization", authorization)
            header("Nav-Call-Id", MDC.get(MDCConstants.MDC_CALL_ID))
            header("Nav-Consumer-Id", CONSUMER_ID)
            header("Nav-Consumer-Token", selvbetjeningstoken)
        }
        install(JsonFeature)
        expectSuccess = false
    }
    val eregResult: HttpResponse = eregClient.request(environment.eregApiUrl.plus("/v1/organisasjon/$orgnr/noekkelinfo"))
    eregClient.close()
    if (eregResult.status.isSuccess()) {
        val eregOrganisasjon = eregResult.receive<EregOrganisasjon>()
        eregOrganisasjon.navn.getNavn()
    } else {
        logger.error("Oppslag mot EREG på organisasjonsnummer $orgnr feilet med melding: ".plus(eregResult.receive<String>()))
        orgnr
    }
}