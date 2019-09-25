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
import io.ktor.client.request.parameter
import io.ktor.request.header
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get
import mu.KLogging
import no.nav.sbl.dsop.api.Environment
import no.nav.sbl.dsop.api.dto.*
import no.nav.sbl.dsop.oppslag.ereg.getOrganisasjonsnavn
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
                    if (!selvbetjeningIdtoken.isNullOrEmpty()) "Bearer " + selvbetjeningIdtoken
                    else call.request.header("Authorization")
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
            val sporingslogg2 = dsopResult.response.receive<List<Sporingslogg2>>()

//            val eregClient = HttpClient() {
//                defaultRequest {
//                    header(env.apiKeyUsername, env.dsopApiEregApiApikeyPassword)
//                    header("Authorization", authorization)
//                    header("Nav-Call-Id", "1234")
//                    header("Nav-Consumer-Id", "dsop-api")
//                    parameter("gyldigDato", "2019-01-01")
//                }
//                install(JsonFeature)
//            }
            val orgnr = "914782007"
//            val eregResult = eregClient.call(env.eregApiUrl.plus("v1/organisasjon/" + orgnr + "/noekkelinfo"))
//            val eregOrganisasjon = eregResult.response.receive<EregOrganisasjon>()
            val orgnavn = getOrganisasjonsnavn(authorization, orgnr)

            call.respond(
                    sporingslogg2.map {
                        Sporingslogg2(
                                tema = it.tema,
                                uthentingsTidspunkt = it.uthentingsTidspunkt,
                                mottaker = it.mottaker,
                                mottakernavn = orgnavn,
                                //mottaker = it.mottaker,
                                //leverteData = String(Base64.getDecoder().decode(it.leverteData)),
                                leverteData = it.leverteData,
                                samtykkeToken = it.samtykkeToken

                        )
                    }
            )
        }
    }

}