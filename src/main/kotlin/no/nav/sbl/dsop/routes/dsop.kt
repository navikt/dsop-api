package no.nav.sbl.dsop.routes

import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get
import mu.KotlinLogging
import no.nav.sbl.dsop.config.OIDC_COOKIE_NAME
import no.nav.sbl.dsop.service.DsopService

private val logger = KotlinLogging.logger {}

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