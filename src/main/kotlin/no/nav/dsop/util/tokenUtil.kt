package no.nav.dsop.util

import io.ktor.server.application.ApplicationCall
import io.ktor.server.request.authorization

private const val OIDC_COOKIE_NAME = "selvbetjening-idtoken"

fun getSelvbetjeningTokenFromCall(call: ApplicationCall): String {
    return call.request.cookies[OIDC_COOKIE_NAME] ?: call.request.authorization()!!
}