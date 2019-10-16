package no.nav.sbl.dsop.oppslag.dsop

import io.ktor.application.call
import io.ktor.client.HttpClient
import io.ktor.client.call.call
import io.ktor.client.call.receive
import io.ktor.client.features.defaultRequest
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.request.header
import io.ktor.request.header
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get
import no.nav.sbl.dsop.api.Environment
import no.nav.sbl.dsop.api.KODEVERK_TEMA_CACHE
import no.nav.sbl.dsop.api.OIDC_COOKIE_NAME
import no.nav.sbl.dsop.api.dto.Sporingslogg
import no.nav.sbl.dsop.oppslag.ereg.getOrganisasjonsnavn
import no.nav.sbl.dsop.oppslag.kodeverk.getKodeverk

fun Route.dsop(env: Environment, mockdata: Any? = null) {
    get("get") {
        val selvbetjeningIdtoken = call.request.cookies[OIDC_COOKIE_NAME]
        val authorization =
                if (env.isLocalhost()) ""
                else if (!selvbetjeningIdtoken.isNullOrEmpty()) "Bearer ".plus(selvbetjeningIdtoken)
                else call.request.header("Authorization")
                        ?: throw IllegalArgumentException("Kunne ikke hente ut brukers OIDC-token.")
        val dsopClient = HttpClient() {
            defaultRequest {
                header(env.apiKeyUsername, env.dsopApiSporingsloggLesloggerApiKeyPassword)
                header("Authorization", authorization)
            }
            install(JsonFeature)
        }

        val dsopResult = dsopClient.call(env.sporingloggLesloggerUrl)
        val sporingslogg2 = dsopResult.response.receive<List<Sporingslogg>>()

        val orgnavnCache = HashMap<String, String>()
        call.respond(
                sporingslogg2.map {
                    if (orgnavnCache.get(it.mottaker) == null) {
                        orgnavnCache.put(it.mottaker, getOrganisasjonsnavn(authorization = authorization, orgnr = it.mottaker, environment = env))
                    }
                    if (KODEVERK_TEMA_CACHE.get(it.tema) == null) {
                        KODEVERK_TEMA_CACHE.put(it.tema, getKodeverk(authorization = authorization, kode = it.tema, environment = env))
                    }

                    Sporingslogg(
                            tema = KODEVERK_TEMA_CACHE.get(it.tema) ?: it.tema,
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
