package no.nav.sbl.dsop.consumer.sporingslogg

import io.ktor.client.HttpClient
import io.ktor.client.call.receive
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.statement.HttpResponse
import io.ktor.http.isSuccess
import kotlinx.coroutines.runBlocking
import no.nav.sbl.dsop.config.Environment
import no.nav.sbl.dsop.consumer.sporingslogg.dto.Sporingslogg


class SporingsloggConsumer(private val client: HttpClient, private val environment: Environment) {

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
                dsopResult.receive()
            } else {
                throw RuntimeException("Kall til sporingslogg feilet med status ${dsopResult.status}: ${dsopResult.receive<String>()}")
            }
        }
}