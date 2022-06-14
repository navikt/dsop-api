package no.nav.sbl.dsop.consumer.ereg

import io.ktor.client.HttpClient
import io.ktor.client.call.receive
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.statement.HttpResponse
import io.ktor.http.isSuccess
import kotlinx.coroutines.runBlocking
import mu.KotlinLogging
import no.nav.sbl.dsop.config.Environment
import no.nav.sbl.dsop.consumer.ereg.dto.EregOrganisasjon


class EregConsumer(private val client: HttpClient, private val environment: Environment) {

    private val logger = KotlinLogging.logger {}

    fun getOrganisasjonsnavn(
        authorization: String,
        selvbetjeningstoken: String,
        orgnr: String,
    ): String =
        runBlocking {
            val eregResult: HttpResponse =
                client.get(environment.eregApiUrl.plus("/v1/organisasjon/$orgnr/noekkelinfo")) {
                    header("Authorization", authorization)
                    header("Nav-Consumer-Token", selvbetjeningstoken)
                }
            if (eregResult.status.isSuccess()) {
                val eregOrganisasjon = eregResult.receive<EregOrganisasjon>()
                eregOrganisasjon.navn.getNavn()
            } else {
                logger.error("Oppslag mot EREG på organisasjonsnummer $orgnr feilet med melding: ".plus(eregResult.receive<String>()))
                orgnr
            }
        }
}