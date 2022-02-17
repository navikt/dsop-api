package no.nav.sbl.dsop.oppslag.dsop

import io.ktor.application.*
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import mu.KotlinLogging
import no.nav.sbl.dsop.api.Environment
import no.nav.sbl.dsop.api.KODEVERK_TEMA_CACHE
import no.nav.sbl.dsop.api.OIDC_COOKIE_NAME
import no.nav.sbl.dsop.api.dto.Sporingslogg
import no.nav.sbl.dsop.oppslag.ereg.getOrganisasjonsnavn
import no.nav.sbl.dsop.oppslag.kodeverk.getKodeverk
import no.nav.tms.token.support.tokendings.exchange.TokendingsService
import kotlin.collections.set

private val logger = KotlinLogging.logger {}

fun Route.dsop(env: Environment, tokendingsService: TokendingsService) {
    get("get") {
        try {
            val selvbetjeningIdtoken = call.request.cookies[OIDC_COOKIE_NAME]
            val tokenxToken =
                tokendingsService.exchangeToken(selvbetjeningIdtoken!!, env.personopplysningerProxyTargetApp)
            val authorization =
                if (env.isMockedEnvironment()) ""
                else if (!selvbetjeningIdtoken.isNullOrEmpty()) "Bearer ".plus(tokenxToken)
                else call.request.header("Authorization")
                    ?: throw IllegalArgumentException("Kunne ikke hente ut brukers OIDC-token.")
            val dsopClient = HttpClient {
                defaultRequest {
                    header("Authorization", authorization)
                    header("Nav-Consumer-Token", selvbetjeningIdtoken)
                }
                install(JsonFeature)
                expectSuccess = false
            }

            val dsopResult: HttpResponse = dsopClient.request(env.sporingloggLesloggerUrl)

            if (dsopResult.status.isSuccess()) {
                val sporingslogg: List<Sporingslogg> = dsopResult.receive()

                val orgnavnCache = HashMap<String, String>()

                call.respond(
                    sporingslogg.map {
                        if (orgnavnCache[it.mottaker] == null) {
                            orgnavnCache[it.mottaker] =
                                getOrganisasjonsnavn(
                                    selvbetjeningstoken = selvbetjeningIdtoken,
                                    authorization = authorization,
                                    orgnr = it.mottaker,
                                    environment = env
                                )
                        }
                        if (KODEVERK_TEMA_CACHE[it.tema] == null) {
                            KODEVERK_TEMA_CACHE[it.tema] =
                                getKodeverk(authorization = authorization, kode = it.tema, environment = env)
                        }

                        Sporingslogg(
                            tema = KODEVERK_TEMA_CACHE[it.tema] ?: it.tema,
                            uthentingsTidspunkt = it.uthentingsTidspunkt,
                            mottaker = it.mottaker,
                            mottakernavn = orgnavnCache[it.mottaker],
                            leverteData = it.leverteData,
                            samtykkeToken = it.samtykkeToken
                        )
                    }
                )
            } else {
                logger.warn("Kall til sporingslogg feilet med status ${dsopResult.status}: ${dsopResult.receive<String>()}")
                call.respond(HttpStatusCode.InternalServerError, HttpStatusCode.InternalServerError.description)
            }

        } catch (e: Exception) {
            logger.error("Noe gikk galt i DsopCall", e)
            call.respond(HttpStatusCode.InternalServerError, HttpStatusCode.InternalServerError.description)
        }
    }
}