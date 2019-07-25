package no.nav.sbl.dsop.api.admin.platform

import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.response.respondText
import io.ktor.routing.Routing
import io.ktor.routing.get


fun Routing.health(
        ready: () -> Boolean = { true },
        alive: () -> Boolean = { true }
) {

    fun statusFor(b: () -> Boolean) = b().let{ if(it) HttpStatusCode.OK else HttpStatusCode.InternalServerError}

    get("internal/isReady") {
        statusFor(ready).let { call.respondText("Ready: $it", status = it) }
    }

    get("internal/isAlive") {
        statusFor(alive).let { call.respondText("Alive: $it", status = it) }
    }

}

