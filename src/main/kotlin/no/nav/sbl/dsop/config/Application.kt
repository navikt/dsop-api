package no.nav.sbl.dsop.config

import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.auth.Authentication
import io.ktor.auth.authenticate
import io.ktor.features.CORS
import io.ktor.features.CallLogging
import io.ktor.features.ContentNegotiation
import io.ktor.features.StatusPages
import io.ktor.gson.gson
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.request.httpMethod
import io.ktor.request.path
import io.ktor.request.uri
import io.ktor.routing.route
import io.ktor.routing.routing
import mu.KotlinLogging
import no.nav.sbl.dsop.consumer.ereg.EregConsumer
import no.nav.sbl.dsop.consumer.kodeverk.KodeverkConsumer
import no.nav.sbl.dsop.consumer.sporingslogg.SporingsloggConsumer
import no.nav.sbl.dsop.health.health
import no.nav.sbl.dsop.routes.dsop
import no.nav.sbl.dsop.service.DsopService
import no.nav.security.token.support.ktor.tokenValidationSupport
import no.nav.tms.token.support.tokendings.exchange.TokendingsServiceBuilder

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

private val logger = KotlinLogging.logger {}

fun Application.module() {
    logger.info("Starting application...")

    val env = Environment()
    val httpClient = HttpClientBuilder.build()

    val tokendingsService = TokendingsServiceBuilder.buildTokendingsService()
    val sporingsloggConsumer = SporingsloggConsumer(httpClient, env, tokendingsService)
    val eregConsumer = EregConsumer(httpClient, env)
    val kodeverkConsumer = KodeverkConsumer(httpClient, env)
    val dsopService = DsopService(sporingsloggConsumer, eregConsumer, kodeverkConsumer)

    install(StatusPages) {
        status(HttpStatusCode.NotFound) { cause ->
            logger.warn(cause.description + ": " + call.request.uri)
        }
    }

    val conf = this.environment.config
    install(Authentication) {
        tokenValidationSupport(config = conf)
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

    install(ContentNegotiation) {
        gson {
            setPrettyPrinting()
        }
    }

    install(CORS) {
        host(env.corsAllowedOrigins, schemes = listOf(env.corsAllowedSchemes))
        allowCredentials = true
        header(HttpHeaders.ContentType)
    }

    routing {
        health()
        route("/") {
            authenticate {
                dsop(dsopService)
            }
        }
    }
}
