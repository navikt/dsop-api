package no.nav.dsop.consumer.kodeverk

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.http.isSuccess
import no.nav.dsop.config.BEARER
import no.nav.dsop.config.Environment
import no.nav.dsop.config.HEADER_AUTHORIZATION
import no.nav.dsop.config.HEADER_NAV_CALL_ID
import no.nav.dsop.consumer.kodeverk.dto.Kodeverk
import no.nav.tms.token.support.azure.exchange.AzureService
import org.slf4j.LoggerFactory
import org.slf4j.MDC
import java.util.*

class KodeverkConsumer(
    private val client: HttpClient,
    private val environment: Environment,
    private val azureService: AzureService,
) {
    private val logger = LoggerFactory.getLogger(KodeverkConsumer::class.java)

    private val kodeverkEndpoint = environment.kodeverkRestApiUrl + KODEVERK_TEMA_PATH

    suspend fun getKodeverk(kode: String): String {
        azureService.getAccessToken(environment.kodeverkTargetApp).let { accessToken ->
            client.get(kodeverkEndpoint) {
                header(HEADER_AUTHORIZATION, BEARER + accessToken)
                header(HEADER_NAV_CALL_ID, MDC.get(MDC_CALL_ID) ?: UUID.randomUUID())
            }.let { result ->
                return if (result.status.isSuccess()) {
                    result.body<Kodeverk>().termBy(kode)
                } else {
                    logger.warn("Oppslag mot KODEVERK på temakode $kode feilet med status ${result.status.value}")
                    kode
                }
            }
        }
    }

    private fun Kodeverk.termBy(kode: String, spraak: String = NORSK_BOKMAAL): String {
        return betydninger[kode]?.get(0)?.beskrivelser?.get(spraak)?.term ?: kode
    }

    companion object {
        private const val MDC_CALL_ID = "callId"
        private const val NORSK_BOKMAAL = "nb"
        private const val KODEVERK_TEMA_PATH =
            "/api/v1/kodeverk/Tema/koder/betydninger?ekskluderUgyldige=true&spraak=$NORSK_BOKMAAL"
    }
}
