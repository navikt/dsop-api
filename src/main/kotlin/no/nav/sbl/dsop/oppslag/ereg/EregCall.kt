package no.nav.sbl.dsop.oppslag.ereg

import io.ktor.client.HttpClient
import io.ktor.client.call.call
import io.ktor.client.call.receive
import io.ktor.client.features.defaultRequest
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.request.header
import kotlinx.coroutines.runBlocking
import no.nav.log.MDCConstants
import no.nav.sbl.dsop.api.CONSUMER_ID
import no.nav.sbl.dsop.api.Environment
import no.nav.sbl.dsop.api.dto.EregOrganisasjon
import org.slf4j.MDC

fun getOrganisasjonsnavn(authorization: String?, orgnr: String?): String = runBlocking {

    val env = Environment()
    val eregClient = HttpClient() {
        defaultRequest {
            header(env.apiKeyUsername, env.dsopApiEregApiApikeyPassword)
            header("Authorization", authorization)
            header("Nav-Call-Id", MDC.get(MDCConstants.MDC_CALL_ID))
            header("Nav-Consumer-Id", CONSUMER_ID)
        }
        install(JsonFeature)
    }
    val eregResult = eregClient.call(env.eregApiUrl.plus("v1/organisasjon/$orgnr/noekkelinfo"))
    val eregOrganisasjon = eregResult.response.receive<EregOrganisasjon>()
    eregOrganisasjon.navn!!.getNavn()
}