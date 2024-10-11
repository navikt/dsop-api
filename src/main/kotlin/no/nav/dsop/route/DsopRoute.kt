package no.nav.dsop.route

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import no.nav.dsop.service.DsopService
import no.nav.dsop.utils.getAuthTokenFromRequest
import org.slf4j.LoggerFactory

private val logger = LoggerFactory.getLogger("dsopRoute")

fun Route.dsop(dsopService: DsopService) {
    get("get") {
        try {
            val authToken = getAuthTokenFromRequest(call.request)
            call.respond(dsopService.getDsop(authToken))
        } catch (e: Exception) {
            logger.error("Noe gikk galt i DsopCall", e)
            call.respond(HttpStatusCode.InternalServerError, HttpStatusCode.InternalServerError.description)
        }
    }
}