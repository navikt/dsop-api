package no.nav.sbl.dsop.api

import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.CORS
import io.ktor.features.ContentNegotiation
import io.ktor.features.StatusPages
import io.ktor.gson.gson
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.request.uri
import io.ktor.routing.route
import io.ktor.routing.routing
import io.ktor.server.engine.ApplicationEngine
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import mu.KLogging
import mu.KotlinLogging
import no.nav.sbl.dsop.api.Bootstrap.start
import no.nav.sbl.dsop.api.admin.platform.health
import no.nav.sbl.dsop.oppslag.dsop.dsop

val CONSUMER_ID = "dsop-api"
val HTTP_STATUS_CODES_2XX = IntRange(200, 299)

fun main(args: Array<String>) {
    start(webApplication())
}

fun webApplication(port: Int = 8080, mockdata: Any? = null): ApplicationEngine {
    return embeddedServer(Netty, port) {
        install(StatusPages) {
            status(HttpStatusCode.NotFound) { cause ->
                KLogging().logger.warn(cause.description + ": " + call.request.uri)
            }
        }
        install(ContentNegotiation) {
            gson {
                setPrettyPrinting()
            }
        }
        install(CORS) {
            host(host = "personopplysninger-q0.nais.oera-q.local", schemes = listOf("https"))
            host(host = "nav.no", subDomains = listOf("www", "www-q0", "www-q1", "personopplysninger-q"), schemes = listOf("https"))
            allowSameOrigin = true
            allowCredentials = true
            allowNonSimpleContentTypes = true
            header(HttpHeaders.Origin)
            header(HttpHeaders.Authorization)
        }

        routing {
            health()
            route("person/dsop-api/") {
                dsop(mockdata)
            }
        }
    }
}

object Bootstrap {
    private val log = KotlinLogging.logger { }

    fun start(webApplication: ApplicationEngine) {
        log.info("Starting web application")
        webApplication.start(wait = true)
    }
}