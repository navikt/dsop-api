package no.nav.sbl.dsop.api

import io.ktor.application.call
import io.ktor.application.install
import io.ktor.auth.Authentication
import io.ktor.auth.authenticate
import io.ktor.auth.jwt.jwt
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
import no.nav.personbruker.dittnav.api.config.setupOidcAuthentication
import no.nav.sbl.dsop.api.Bootstrap.start
import no.nav.sbl.dsop.api.admin.platform.health
import no.nav.sbl.dsop.oppslag.dsop.dsop
import java.util.*
import kotlin.concurrent.scheduleAtFixedRate

fun main(args: Array<String>) {
    startCacheEvictScheduling()
    start(webApplication())
}

fun webApplication(port: Int = 8080, mockdata: Any? = null, env: Environment = Environment()): ApplicationEngine {
    return embeddedServer(Netty, port) {
        install(StatusPages) {
            status(HttpStatusCode.NotFound) { cause ->
                KLogging().logger.warn(cause.description + ": " + call.request.uri)
            }
        }
        install(Authentication) {
            jwt {
                setupOidcAuthentication(env)
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
                authenticate {
                    dsop(mockdata)
                }
            }
        }
    }
}

private fun startCacheEvictScheduling() {
    val timer = Timer("kodeverk-cache-clear-task", true)
    timer.scheduleAtFixedRate(KODEVERK_TEMA_CACHE_CLEARING_INTERVAL, KODEVERK_TEMA_CACHE_CLEARING_INTERVAL) {
        KLogging().logger.info("Clearing cache...")
        KODEVERK_TEMA_CACHE.clear()
    }
}

object Bootstrap {
    private val log = KotlinLogging.logger { }
    fun start(webApplication: ApplicationEngine) {
        log.info("Starting web application")
        webApplication.start(wait = true)
    }
}