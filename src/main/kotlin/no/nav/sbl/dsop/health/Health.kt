package no.nav.sbl.dsop.health

import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.response.respondText
import io.ktor.routing.Routing
import io.ktor.routing.get
import io.ktor.routing.route


fun Routing.health(
    ready: () -> Boolean = { true },
    alive: () -> Boolean = { true },
) {

    fun statusFor(b: () -> Boolean) = b().let { if (it) HttpStatusCode.OK else HttpStatusCode.InternalServerError }

    route("/internal") {

        get("/isReady") {
            statusFor(ready).let { call.respondText("Ready: $it", status = it) }
        }

        get("/isAlive") {
            statusFor(alive).let { call.respondText("Alive: $it", status = it) }
        }
    }

}

