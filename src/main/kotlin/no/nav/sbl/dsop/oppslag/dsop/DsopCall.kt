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

            val eregClient = HttpClient() {
                defaultRequest {
                    header(env.apiKeyUsername, env.dsopApiEregApiApikeyPassword)
                    header("Authorization", authorization)
                    header("Nav-Call-Id", "1234")
                    header("Nav-Consumer-Id", "dsop-api")
                    parameter("gyldigDato", "2019-01-01")
                }
            }
            val orgnr = "914782007"
            val eregResult = eregClient.call(env.eregApiUrl.plus("v1/organisasjon/" + orgnr + "/noekkelinfo"))
            val resp = eregResult.response.receive<String>()
            KLogging().logger.warn("Response: ".plus(resp))
            //val eregOrganisasjon = eregResult.response.receive<EregOrganisasjon>()


            call.respond(
                    sporingslogg2.map {
                        Sporingslogg2(
                                tema = it.tema,
                                uthentingsTidspunkt = it.uthentingsTidspunkt,
                                //mottaker = it.mottaker.plus(eregOrganisasjon.navn?.navnelinje1),
                                mottaker = it.mottaker,
                                //leverteData = String(Base64.getDecoder().decode(it.leverteData)),
                                leverteData = it.leverteData,
                                samtykkeToken = it.samtykkeToken

                        )
                    }
            )
            //call.respond(sporingslogg2)
            //call.respond(dsopResult.response.receive<String>())

//            val vedtaksListe = mutableListOf<Vedtak>()
//            vedtaksListe.add(Vedtak(
//                    vedtakId = "1",
//                    vedtakstype = "Endring",
//                    vedtaksstatus = "Iverksatt",
//                    vedtaksperiode = Periode(fra = LocalDate.now(), til = LocalDate.now()),
//                    vedtaksvariant = "",
//                    rettighetstype = "Arbeidsavklaringspenger",
//                    aktivitetsfase = "Under arbeidsavklaring",
//                    utfall = "Ja"
//            ))
//            vedtaksListe.add(Vedtak(
//                    vedtakId = "2",
//                    vedtakstype = "Endring",
//                    vedtaksstatus = "Iverksatt",
//                    vedtaksperiode = Periode(fra = LocalDate.now(), til = LocalDate.now()),
//                    vedtaksvariant = "",
//                    rettighetstype = "Arbeidsavklaringspenger",
//                    aktivitetsfase = "Under arbeidsavklaring",
//                    utfall = "Ja"
//            ))
//            val leverteData = LeverteData(uttrekksperiode = Periode(fra = LocalDate.now(), til = LocalDate.now()), vedtak = vedtaksListe)
//            val sporingslogg = Sporingslogg(
//                    tema = "Arbeidsavklaringspenger",
//                    uthentingsTidspunkt = LocalDateTime.now(),
//                    mottaker = "Forsikring AS",
//                    behandlingsgrunnlag = "Hjemmel",
//                    leverteData = leverteData)
//            call.respond(sporingslogg)

            
//            var responseHeaders = dsopResult.response.headers
//            val responseContentType = responseHeaders[HttpHeaders.ContentType]
//            val responseContentLength = responseHeaders[HttpHeaders.ContentLength]
//            val responseStatusCode = dsopResult.response.status
//
//            call.respond(object : OutgoingContent.WriteChannelContent() {
//                override val contentLength: Long? = responseContentLength?.toLong()
//                override val contentType: ContentType = responseContentType?.let { ContentType.parse(it) }
//                        ?: ContentType.Application.Json
//                override val status: HttpStatusCode? = responseStatusCode
//                override suspend fun writeTo(channel: ByteWriteChannel) {
//                    dsopResult.response.content.copyAndClose(channel)
//                }
//            })
        }
    }

}