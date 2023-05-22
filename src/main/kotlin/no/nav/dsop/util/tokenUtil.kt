package no.nav.dsop.util

import io.ktor.server.request.ApplicationRequest
import io.ktor.server.request.authorization

fun getAuthTokenFromRequest(request: ApplicationRequest): String {
    return request.authorization()?.replace("Bearer ", "")
        ?: throw RuntimeException("Kunne ikke utlede token fra request")
}