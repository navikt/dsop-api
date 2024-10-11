package no.nav.dsop.utils

import io.ktor.server.request.ApplicationRequest
import io.ktor.server.request.authorization

fun getAuthTokenFromRequest(request: ApplicationRequest): String {
    return requireNotNull(request.authorization()).replace("Bearer ", "")
}