package no.nav.sbl.dsop.api

import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializer
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.ContentNegotiation
import io.ktor.gson.gson
import io.ktor.response.respond
import io.ktor.routing.RoutingPath
import io.ktor.routing.get
import io.ktor.routing.route
import io.ktor.routing.routing
import io.ktor.server.engine.ApplicationEngine
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import mu.KotlinLogging
import no.nav.sbl.dsop.api.Bootstrap.start
import no.nav.sbl.dsop.api.admin.platform.health
import no.nav.sbl.dsop.api.service.SporingsloggProxy
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

fun main(args: Array<String>) {
    start(webApplication())
}

fun webApplication(port: Int = 8080, proxy: SporingsloggProxy = SporingsloggProxy()): ApplicationEngine {
    return embeddedServer(Netty, port) {
        install(ContentNegotiation) {
            gson {
                setPrettyPrinting()
                registerTypeAdapter(LocalDateTime::class.java, JsonSerializer<LocalDateTime> { localDateTime, _, _ ->
                    JsonPrimitive(DateTimeFormatter.ISO_INSTANT.format(localDateTime.atOffset(ZoneOffset.UTC).toInstant()))
                })
                registerTypeAdapter(LocalDate::class.java, JsonSerializer<LocalDate> { localDate, _, _ ->
                    JsonPrimitive(DateTimeFormatter.ISO_DATE.format(localDate.atStartOfDay()))
                })
            }
        }

        routing {
            health()
            route("person/dsop-api/") {
                get("get/{id}") {
                    call.respond(proxy.hentSporingslogg())
                }
            }
        }
    }
}

object Bootstrap {

    private val log = KotlinLogging.logger { }

    fun start(webApplication: ApplicationEngine) {
        log.debug("Starting web application")
        webApplication.start(wait = true)
    }
}