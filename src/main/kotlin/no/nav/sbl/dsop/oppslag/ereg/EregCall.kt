package no.nav.sbl.dsop.oppslag.ereg

import io.ktor.client.HttpClient
import io.ktor.client.call.call
import io.ktor.client.call.receive
import io.ktor.client.features.defaultRequest
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import kotlinx.coroutines.runBlocking
import no.nav.sbl.dsop.api.Environment
import no.nav.sbl.dsop.api.dto.EregOrganisasjon

fun getOrganisasjonsnavn(authorization: String?, orgnr: String?): String = runBlocking {
    val env = Environment()
    val eregClient = HttpClient() {
        defaultRequest {
            header(env.apiKeyUsername, env.dsopApiEregApiApikeyPassword)
            header("Authorization", authorization)
            header("Nav-Call-Id", "1234")
            header("Nav-Consumer-Id", "dsop-api")
            parameter("gyldigDato", "2019-01-01")
        }
        install(JsonFeature)
    }
    val eregResult = eregClient.call(env.eregApiUrl.plus("v1/organisasjon/" + orgnr + "/noekkelinfo"))
    val eregOrganisasjon = eregResult.response.receive<EregOrganisasjon>()
    eregOrganisasjon.navn?.navnelinje1 ?: ""
}