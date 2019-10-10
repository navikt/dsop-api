package no.nav.sbl.dsop.oppslag.kodeverk

import io.ktor.client.HttpClient
import io.ktor.client.call.call
import io.ktor.client.call.receive
import io.ktor.client.features.defaultRequest
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.request.header
import kotlinx.coroutines.runBlocking
import mu.KLogging
import no.nav.sbl.dsop.api.CONSUMER_ID
import no.nav.sbl.dsop.api.Environment
import no.nav.sbl.dsop.api.HTTP_STATUS_CODES_2XX
import no.nav.sbl.dsop.api.dto.Kodeverk

fun getKodeverk(authorization: String, kode: String, testClient: HttpClient? = null, environment: Environment): String = runBlocking {
    val spraak = "nb"
    val kodeverkClient = testClient ?:  HttpClient() {
        defaultRequest {
            header(environment.apiKeyUsername, environment.dsopApiKodeverkRestApiApikeyPassword)
            header("Authorization", authorization)
            header("Nav-Call-Id", "dsop-api-temakode $kode")
            header("Nav-Consumer-Id", CONSUMER_ID)
        }
        install(JsonFeature)
        expectSuccess = false
    }
    val kodeverkResult = kodeverkClient.call(environment.kodeverkRestApiUrl
            .plus("v1/kodeverk/Tema/koder/betydninger?ekskluderUgyldige=true&spraak=$spraak"))
    kodeverkClient.close()
    if (HTTP_STATUS_CODES_2XX.contains(kodeverkResult.response.status.value)) {
        val kodeverk = kodeverkResult.response.receive<Kodeverk>()
        kodeverk.betydninger[kode]?.get(0)?.beskrivelser?.get(spraak)?.term ?: kode
    } else {
        KLogging().logger.error("Oppslag mot KODEVERK på temakode $kode feilet. "
                .plus(kodeverkResult.response.status.value).plus(" ").plus(kodeverkResult.response.receive<String>()))
        kode
    }
}