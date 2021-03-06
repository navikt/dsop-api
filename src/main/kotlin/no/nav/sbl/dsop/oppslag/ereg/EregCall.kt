package no.nav.sbl.dsop.oppslag.ereg

import io.ktor.client.HttpClient
import io.ktor.client.call.call
import io.ktor.client.call.receive
import io.ktor.client.features.defaultRequest
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.request.header
import kotlinx.coroutines.runBlocking
import mu.KLogging
import no.nav.log.MDCConstants
import no.nav.sbl.dsop.api.CONSUMER_ID
import no.nav.sbl.dsop.api.Environment
import no.nav.sbl.dsop.api.HTTP_STATUS_CODES_2XX
import no.nav.sbl.dsop.api.dto.EregOrganisasjon
import org.slf4j.MDC

fun getOrganisasjonsnavn(authorization: String, orgnr: String, testClient: HttpClient? = null, environment: Environment): String = runBlocking {
    val eregClient = testClient ?:  HttpClient() {
        defaultRequest {
            header(environment.apiKeyUsername, environment.dsopApiEregApiApikeyPassword)
            header("Authorization", authorization)
            header("Nav-Call-Id", MDC.get(MDCConstants.MDC_CALL_ID))
            header("Nav-Consumer-Id", CONSUMER_ID)
        }
        install(JsonFeature)
        expectSuccess = false
    }
    val eregResult = eregClient.call(environment.eregApiUrl.plus("v1/organisasjon/$orgnr/noekkelinfo"))
    eregClient.close()
    if (HTTP_STATUS_CODES_2XX.contains(eregResult.response.status.value)) {
        val eregOrganisasjon = eregResult.response.receive<EregOrganisasjon>()
        eregOrganisasjon.navn.getNavn()
    } else {
        KLogging().logger.error("Oppslag mot EREG på organisasjonsnummer $orgnr feilet med melding: ".plus(eregResult.response.receive<String>()))
        orgnr
    }
}