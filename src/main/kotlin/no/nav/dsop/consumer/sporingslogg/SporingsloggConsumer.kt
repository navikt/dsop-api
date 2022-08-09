package no.nav.dsop.consumer.sporingslogg

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.statement.HttpResponse
import io.ktor.http.isSuccess
import no.nav.dsop.config.Environment
import no.nav.dsop.consumer.sporingslogg.dto.Sporingslogg
import no.nav.tms.token.support.tokendings.exchange.TokendingsService


class SporingsloggConsumer(
    private val client: HttpClient,
    private val environment: Environment,
    private val tokendingsService: TokendingsService
) {

    suspend fun getSporingslogg(selvbetjeningstoken: String): List<Sporingslogg> {
        val accessToken = tokendingsService.exchangeToken(selvbetjeningstoken, environment.sporingsloggTargetApp)
        val dsopResult: HttpResponse = client.get(environment.sporingloggLesloggerUrl) {
            header("Authorization", "Bearer $accessToken")
        }

        return if (dsopResult.status.isSuccess()) {
            dsopResult.body()
        } else {
            throw RuntimeException("Kall til sporingslogg feilet med status ${dsopResult.status}: ${dsopResult.body<String>()}")
        }
    }
}