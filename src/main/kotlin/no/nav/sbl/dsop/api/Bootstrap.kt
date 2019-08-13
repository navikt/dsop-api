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
import io.ktor.features.StatusPages
import io.ktor.gson.gson
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.OutgoingContent
import io.ktor.request.header
import io.ktor.request.uri
import io.ktor.response.respond
import io.ktor.routing.get
import io.ktor.routing.route
import io.ktor.routing.routing
import io.ktor.server.engine.ApplicationEngine
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import kotlinx.coroutines.io.ByteWriteChannel
import kotlinx.coroutines.io.copyAndClose
import mu.KLogging
import mu.KotlinLogging
import no.nav.sbl.dsop.api.Bootstrap.start
import no.nav.sbl.dsop.api.admin.platform.health
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

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
                get("get") {
                    if (mockdata != null) {
                        call.respond(mockdata)
                    } else {
                        val env = Environment()
                        val selvbetjeningIdtoken = call.request.cookies["selvbetjening-idtoken"]
                        val authorization =
                                if (!selvbetjeningIdtoken.isNullOrEmpty()) "Bearer " + selvbetjeningIdtoken
                                else call.request.header("Authorization")
                        val dsopClient = HttpClient() {
                            defaultRequest {
                                header(env.dsopApiSporingsloggLesloggerApiKeyUsername, env.dsopApiSporingsloggLesloggerApiKeyPassword)
                                header("Authorization", authorization)
                            }
                        }

                        val dsopResult = dsopClient.call(env.sporingloggLesloggerUrl)
                        var responseHeaders = dsopResult.response.headers
                        val responseContentType = responseHeaders[HttpHeaders.ContentType]
                        val responseContentLength = responseHeaders[HttpHeaders.ContentLength]
                        val responseStatusCode = dsopResult.response.status

                        call.respond(object : OutgoingContent.WriteChannelContent() {
                            override val contentLength: Long? = responseContentLength?.toLong()
                            override val contentType: ContentType = responseContentType?.let { ContentType.parse(it) }
                                    ?: ContentType.Application.Json
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
}

object Bootstrap {
    private val log = KotlinLogging.logger { }

    fun start(webApplication: ApplicationEngine) {
        log.info("Starting web application")
        webApplication.start(wait = true)
    }
}