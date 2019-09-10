package no.nav.sbl.dsop.oppslag.dsop

import io.ktor.application.call
import io.ktor.client.HttpClient
import io.ktor.client.call.call
import io.ktor.client.features.defaultRequest
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.OutgoingContent
import io.ktor.request.header
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get
import kotlinx.coroutines.io.ByteWriteChannel
import kotlinx.coroutines.io.copyAndClose
import no.nav.sbl.dsop.api.Environment

fun Route.dsop(mockdata: Any? = null) {
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