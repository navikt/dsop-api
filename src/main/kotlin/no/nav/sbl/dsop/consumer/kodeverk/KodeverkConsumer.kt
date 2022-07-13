package no.nav.sbl.dsop.consumer.kodeverk

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.statement.HttpResponse
import io.ktor.http.isSuccess
import kotlinx.coroutines.runBlocking
import no.nav.sbl.dsop.config.Environment
import no.nav.sbl.dsop.consumer.kodeverk.dto.Kodeverk
import org.slf4j.LoggerFactory
import org.slf4j.MDC
import java.util.*

class KodeverkConsumer(private val client: HttpClient, private val environment: Environment) {

    private val MDC_CALL_ID = "callId"

    private val logger = LoggerFactory.getLogger(KodeverkConsumer::class.java)

    fun getKodeverk(kode: String): String =
        runBlocking {
            val spraak = "nb"
            val kodeverkResult: HttpResponse = client.get(
                environment.kodeverkRestApiUrl
                    .plus("/api/v1/kodeverk/Tema/koder/betydninger?ekskluderUgyldige=true&spraak=$spraak")
            ) {
                header("Nav-Call-Id", MDC.get(MDC_CALL_ID) ?: UUID.randomUUID())
            }
            if (kodeverkResult.status.isSuccess()) {
                val kodeverk = kodeverkResult.body<Kodeverk>()
                kodeverk.betydninger[kode]?.get(0)?.beskrivelser?.get(spraak)?.term ?: kode
            } else {
                logger.warn(
                    "Oppslag mot KODEVERK på temakode $kode feilet. "
                        .plus(kodeverkResult.status.value).plus(" ")
                        .plus(kodeverkResult.body<String>())
                )
                kode
            }
        }
}