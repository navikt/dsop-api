package no.nav.sbl.dsop.oppslag.kodeverk

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import mu.KotlinLogging
import no.nav.sbl.dsop.api.CONSUMER_ID
import no.nav.sbl.dsop.api.Environment
import no.nav.sbl.dsop.api.dto.Kodeverk

private val logger = KotlinLogging.logger {}

fun getKodeverk(kode: String, testClient: HttpClient? = null, environment: Environment): String =
    runBlocking {
        val spraak = "nb"
        val kodeverkClient = testClient ?: HttpClient {
            defaultRequest {
                header("Nav-Call-Id", "dsop-api-temakode $kode")
                header("Nav-Consumer-Id", CONSUMER_ID)
            }
            install(JsonFeature)
            expectSuccess = false
        }
        val kodeverkResult: HttpResponse = kodeverkClient.request(
            environment.kodeverkRestApiUrl
                .plus("/api/v1/kodeverk/Tema/koder/betydninger?ekskluderUgyldige=true&spraak=$spraak")
        )
        kodeverkClient.close()
        if (kodeverkResult.status.isSuccess()) {
            val kodeverk = kodeverkResult.receive<Kodeverk>()
            kodeverk.betydninger[kode]?.get(0)?.beskrivelser?.get(spraak)?.term ?: kode
        } else {
            logger.error(
                "Oppslag mot KODEVERK på temakode $kode feilet. "
                    .plus(kodeverkResult.status.value).plus(" ")
                    .plus(kodeverkResult.receive<String>())
            )
            kode
        }
    }
