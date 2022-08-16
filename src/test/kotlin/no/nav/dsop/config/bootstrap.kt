package no.nav.dsop.config

import io.ktor.serialization.gson.gson
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.routing.routing
import no.nav.dsop.config.mock.setupMockedClient
import no.nav.dsop.routes.dsop


fun Application.testModule(appContext: TestApplicationContext) {

    install(ContentNegotiation) {
        gson {
            setPrettyPrinting()
        }
    }

    routing {
        dsop(appContext.dsopService)
    }
}

fun main() {
    // Todo: logging, auto-reload, kanskje flytte til egen fil
    embeddedServer(Netty, port = 8080) {
        testModule(TestApplicationContext(setupMockedClient()))
    }.start(wait = true)
}