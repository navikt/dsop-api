package no.nav.sbl.dsop.routes

import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.request.header
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get
import mu.KotlinLogging
import no.nav.sbl.dsop.config.Environment
import no.nav.sbl.dsop.config.OIDC_COOKIE_NAME
import no.nav.sbl.dsop.service.DsopService
import no.nav.tms.token.support.tokendings.exchange.TokendingsService

private val logger = KotlinLogging.logger {}

fun Route.dsop(env: Environment, tokendingsService: TokendingsService, dsopService: DsopService) {
    get("get") {
        try {
            val selvbetjeningIdtoken = call.request.cookies[OIDC_COOKIE_NAME]
            val tokenxToken =
                tokendingsService.exchangeToken(selvbetjeningIdtoken!!, env.personopplysningerProxyTargetApp)
            val authorization =
                if (!selvbetjeningIdtoken.isNullOrEmpty()) "Bearer ".plus(tokenxToken)
                else call.request.header("Authorization")
                    ?: throw IllegalArgumentException("Kunne ikke hente ut brukers OIDC-token.")

            call.respond(dsopService.getDsop(authorization, selvbetjeningIdtoken))

        } catch (e: Exception) {
            logger.error("Noe gikk galt i DsopCall", e)
            call.respond(HttpStatusCode.InternalServerError, HttpStatusCode.InternalServerError.description)
        }
    }
}