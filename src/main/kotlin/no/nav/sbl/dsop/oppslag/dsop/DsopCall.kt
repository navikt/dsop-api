package no.nav.sbl.dsop.oppslag.dsop

import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializer
import io.ktor.application.call
import io.ktor.client.HttpClient
import io.ktor.client.call.call
import io.ktor.client.call.receive
import io.ktor.client.features.defaultRequest
import io.ktor.client.features.json.GsonSerializer
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.request.header
import io.ktor.request.header
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get
import no.nav.sbl.dsop.api.Environment
import no.nav.sbl.dsop.api.dto.Sporingslogg
import no.nav.sbl.dsop.oppslag.ereg.getOrganisasjonsnavn
import java.lang.IllegalArgumentException
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

fun Route.dsop(mockdata: Any? = null) {
    get("get") {
        if (mockdata != null) {
            call.respond(mockdata)
        } else {
            val env = Environment()
            val selvbetjeningIdtoken = call.request.cookies["selvbetjening-idtoken"]
            val authorization =
                    if (!selvbetjeningIdtoken.isNullOrEmpty()) "Bearer ".plus(selvbetjeningIdtoken)
                    else call.request.header("Authorization") ?: throw IllegalArgumentException("Kunne ikke hente ut brukers OIDC-token.")
            val dsopClient = HttpClient() {
                defaultRequest {
                    header(env.apiKeyUsername, env.dsopApiSporingsloggLesloggerApiKeyPassword)
                    header("Authorization", authorization)
                }
                install(JsonFeature) {
                    serializer = GsonSerializer() {
                        registerTypeAdapter(LocalDateTime::class.java, JsonSerializer<LocalDateTime> { localDateTime, _, _ ->
                            JsonPrimitive(DateTimeFormatter.ISO_INSTANT.format(localDateTime.atOffset(ZoneOffset.UTC).toInstant()))
                        })
                    }
                }
            }

            val dsopResult = dsopClient.call(env.sporingloggLesloggerUrl)
            val sporingslogg2 = dsopResult.response.receive<List<Sporingslogg>>()

            val orgnavnCache = HashMap<String, String>()
            call.respond(
                sporingslogg2.map {
                    if (it.mottaker != null && orgnavnCache.get(it.mottaker) == null) {
                        orgnavnCache.put(it.mottaker, getOrganisasjonsnavn(authorization, it.mottaker))
                    }

                    Sporingslogg(
                        tema = it.tema,
                        uthentingsTidspunkt = it.uthentingsTidspunkt,
                        mottaker = it.mottaker,
                        mottakernavn = orgnavnCache.get(it.mottaker),
                        leverteData = it.leverteData,
                        samtykkeToken = it.samtykkeToken
                    )
                }
            )
        }
    }
}