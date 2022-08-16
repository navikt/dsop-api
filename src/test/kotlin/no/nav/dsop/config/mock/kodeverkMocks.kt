package no.nav.dsop.config.mock

import io.ktor.client.engine.mock.MockRequestHandleScope
import io.ktor.client.engine.mock.respond
import io.ktor.client.engine.mock.respondError
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.http.isSuccess


fun MockRequestHandleScope.mockKodeverk(status: HttpStatusCode) =
    if (status.isSuccess()) {
        respond(
            readJson("/json/kodeverk-tema.json"),
            headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString())
        )
    } else {
        respondError(HttpStatusCode.InternalServerError)
    }