package no.nav.sbl.dsop.api

import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializer
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.ContentNegotiation
import io.ktor.gson.gson
import io.ktor.response.respond
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.server.engine.ApplicationEngine
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import mu.KotlinLogging
import no.nav.sbl.dsop.api.Bootstrap.start
import no.nav.sbl.dsop.api.admin.platform.health
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

fun main(args: Array<String>) {
    start(webApplication())
}

fun webApplication(port: Int = 8080): ApplicationEngine {
    return embeddedServer(Netty, port) {
        install(ContentNegotiation) {
            gson {
                setPrettyPrinting()
                registerTypeAdapter(LocalDateTime::class.java, JsonSerializer<LocalDateTime> { localDateTime, _, _ ->
                    JsonPrimitive(DateTimeFormatter.ISO_INSTANT.format(localDateTime.atOffset(ZoneOffset.UTC).toInstant()))
                })
            }
        }
        routing {
            health()
            get("get/{id}") {
                call.respond(call.parameters["id"]!!.toInt() ?: "")
            }
        }
    }
}

object Bootstrap {

    private val log = KotlinLogging.logger { }

    fun start(webApplication: ApplicationEngine) {
        log.debug("Starting weg application")
        webApplication.start(wait = true)
    }
}