package no.nav.sbl.dsop.api

import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializer
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.client.HttpClient
import io.ktor.client.call.call
import io.ktor.client.features.defaultRequest
import io.ktor.client.request.header
import io.ktor.features.ContentNegotiation
import io.ktor.gson.gson
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.OutgoingContent
import io.ktor.request.header
import io.ktor.response.respond
import io.ktor.routing.get
import io.ktor.routing.route
import io.ktor.routing.routing
import io.ktor.server.engine.ApplicationEngine
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import kotlinx.coroutines.io.ByteWriteChannel
import kotlinx.coroutines.io.copyAndClose
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

    var DSOP_API_SPORINGSLOGG_LESLOGGER_API_KEY_USERNAME: String = System.getenv("DSOP_API_SPORINGSLOGG_LESLOGGER_API_KEY_USERNAME")
    var DSOP_API_SPORINGSLOGG_LESLOGGER_API_KEY_PASSWORD: String = System.getenv("DSOP_API_SPORINGSLOGG_LESLOGGER_API_KEY_PASSWORD")
    var SPORINGSLOGG_LESLOGGER_URL: String = System.getenv("SPORINGSLOGG_LESLOGGER_URL")


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
            route("person/dsop-api/") {
                get("get") {
                    call.request.cookies.rawCookies.forEach {
                        System.out.println("Cookie: " + it.key + "=" + it.value)}

                    val dsopClient = HttpClient(){
                        defaultRequest {
                            header(DSOP_API_SPORINGSLOGG_LESLOGGER_API_KEY_USERNAME, DSOP_API_SPORINGSLOGG_LESLOGGER_API_KEY_PASSWORD)
                            header("Authorization", "Bearer " + call.request.cookies["selvbetjening-idporten"] ?: call.request.header("Authorization"))
                        }
                    }

                    val dsopResult = dsopClient.call(SPORINGSLOGG_LESLOGGER_URL)
                    var responseHeaders = dsopResult.response.headers
                    val responseContentType = responseHeaders[HttpHeaders.ContentType]
                    val responseContentLength = responseHeaders[HttpHeaders.ContentLength]
                    val responseStatusCode = dsopResult.response.status

                    call.respond(object : OutgoingContent.WriteChannelContent() {
                        override val contentLength: Long? = responseContentLength?.toLong()
                        override val contentType: ContentType = responseContentType?.let { ContentType.parse(it) } ?: ContentType.Application.Json
                        override val status: HttpStatusCode? = responseStatusCode
                        override suspend fun writeTo(channel: ByteWriteChannel) {
                            dsopResult.response.content.copyAndClose(channel)
                        }
                    })
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