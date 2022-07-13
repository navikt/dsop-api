package no.nav.sbl.dsop.routes

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.request.header
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import no.nav.sbl.dsop.config.Environment
import no.nav.sbl.dsop.config.OIDC_COOKIE_NAME
import no.nav.sbl.dsop.service.DsopService
import no.nav.tms.token.support.tokendings.exchange.TokendingsService
import org.slf4j.LoggerFactory

private val logger = LoggerFactory.getLogger("dsopRoute")

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