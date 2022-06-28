package no.nav.sbl.dsop.consumer.sporingslogg

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.statement.HttpResponse
import io.ktor.http.isSuccess
import kotlinx.coroutines.runBlocking
import mu.KotlinLogging
import no.nav.sbl.dsop.config.Environment
import no.nav.sbl.dsop.consumer.sporingslogg.dto.Sporingslogg


class SporingsloggConsumer(private val client: HttpClient, private val environment: Environment) {

    private val logger = KotlinLogging.logger {}

    fun getSporingslogg(
        authorization: String,
        selvbetjeningstoken: String,
    ): List<Sporingslogg> =
        runBlocking {
            val dsopResult: HttpResponse = client.get(environment.sporingloggLesloggerUrl) {
                header("Authorization", authorization)
                header("Nav-Consumer-Token", selvbetjeningstoken)
            }

            if (dsopResult.status.isSuccess()) {
                dsopResult.body()
            } else {
                logger.warn("Kall til sporingslogg feilet med status ${dsopResult.status}: ${dsopResult.body<String>()}")
                throw RuntimeException("Feil ved henting av sporingslogg")
            }
        }
}