package no.nav.dsop.consumer.ereg

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.http.isSuccess
import no.nav.dsop.config.Environment
import no.nav.dsop.consumer.ereg.dto.EregOrganisasjon
import org.slf4j.LoggerFactory


class EregConsumer(private val client: HttpClient, private val environment: Environment) {

    private val logger = LoggerFactory.getLogger(EregConsumer::class.java)

    suspend fun getOrganisasjonsnavn(orgnr: String): String {
        val eregResult: HttpResponse =
            client.get(environment.eregApiUrl.plus("/v1/organisasjon/$orgnr/noekkelinfo"))
        return if (eregResult.status.isSuccess()) {
            val eregOrganisasjon = eregResult.body<EregOrganisasjon>()
            eregOrganisasjon.navn.getNavn()
        } else {
            logger.warn("Oppslag mot EREG på organisasjonsnummer $orgnr feilet med melding: ".plus(eregResult.body<String>()))
            orgnr
        }
    }
}