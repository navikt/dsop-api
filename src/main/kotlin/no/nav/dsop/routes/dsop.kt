package no.nav.dsop.routes

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import no.nav.dsop.config.OIDC_COOKIE_NAME
import no.nav.dsop.service.DsopService
import org.slf4j.LoggerFactory

private val logger = LoggerFactory.getLogger("dsopRoute")

fun Route.dsop(dsopService: DsopService) {
    get("get") {
        try {
            val selvbetjeningIdtoken = call.request.cookies[OIDC_COOKIE_NAME]!!

            call.respond(dsopService.getDsop(selvbetjeningIdtoken))

        } catch (e: Exception) {
            logger.error("Noe gikk galt i DsopCall", e)
            call.respond(HttpStatusCode.InternalServerError, HttpStatusCode.InternalServerError.description)
        }
    }
}