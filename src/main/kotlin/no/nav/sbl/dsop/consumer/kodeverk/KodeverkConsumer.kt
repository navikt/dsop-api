package no.nav.sbl.dsop.consumer.kodeverk

import io.ktor.client.HttpClient
import io.ktor.client.call.receive
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.statement.HttpResponse
import io.ktor.http.isSuccess
import kotlinx.coroutines.runBlocking
import mu.KotlinLogging
import no.nav.common.log.MDCConstants
import no.nav.sbl.dsop.config.Environment
import no.nav.sbl.dsop.consumer.kodeverk.dto.Kodeverk
import org.slf4j.MDC
import java.util.*

class KodeverkConsumer(private val client: HttpClient, private val environment: Environment) {

    private val logger = KotlinLogging.logger {}

    fun getKodeverk(kode: String): String =
        runBlocking {
            val spraak = "nb"
            val kodeverkResult: HttpResponse = client.get(
                environment.kodeverkRestApiUrl
                    .plus("/api/v1/kodeverk/Tema/koder/betydninger?ekskluderUgyldige=true&spraak=$spraak")
            ) {
                header("Nav-Call-Id", MDC.get(MDCConstants.MDC_CALL_ID) ?: UUID.randomUUID())
            }
            if (kodeverkResult.status.isSuccess()) {
                val kodeverk = kodeverkResult.receive<Kodeverk>()
                kodeverk.betydninger[kode]?.get(0)?.beskrivelser?.get(spraak)?.term ?: kode
            } else {
                logger.warn(
                    "Oppslag mot KODEVERK på temakode $kode feilet. "
                        .plus(kodeverkResult.status.value).plus(" ")
                        .plus(kodeverkResult.receive<String>())
                )
                kode
            }
        }
}