package no.nav.sbl.dsop.api

import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.auth.Authentication
import io.ktor.auth.authenticate
import io.ktor.features.CORS
import io.ktor.features.ContentNegotiation
import io.ktor.features.StatusPages
import io.ktor.gson.gson
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.request.uri
import io.ktor.routing.route
import io.ktor.routing.routing
import mu.KLogging
import no.nav.sbl.dsop.api.admin.platform.health
import no.nav.sbl.dsop.oppslag.dsop.dsop
import no.nav.security.token.support.ktor.tokenValidationSupport
import java.util.*
import kotlin.concurrent.scheduleAtFixedRate

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

fun Application.module() {
    KLogging().logger.info("Starting application...")

    val env = Environment()
    startCacheEvictScheduling()

    install(StatusPages) {
        status(HttpStatusCode.NotFound) { cause ->
            KLogging().logger.warn(cause.description + ": " + call.request.uri)
        }
    }

    val conf = this.environment.config

    install(Authentication) {
        if (env.isLocalhost()) {
            provider { skipWhen { true } }
        } else {
            tokenValidationSupport(config = conf)
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
                dsop(env)
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
