package no.nav.dsop.config

import io.ktor.serialization.gson.gson
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.callloging.CallLogging
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.request.httpMethod
import io.ktor.server.request.path
import io.ktor.server.routing.routing
import no.nav.dsop.routes.dsop


fun Application.testModule(appContext: TestApplicationContext) {

    install(ContentNegotiation) {
        gson {
            setPrettyPrinting()
        }
    }

    install(CallLogging) {
        filter { call -> !call.request.path().contains("internal") }
        format { call ->
            val status = call.response.status()
            val httpMethod = call.request.httpMethod.value
            val path = call.request.path()
            "$status - $httpMethod $path"
        }
    }

    routing {
        dsop(appContext.dsopService)
    }
}