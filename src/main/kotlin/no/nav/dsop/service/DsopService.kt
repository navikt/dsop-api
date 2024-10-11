package no.nav.dsop.service

import no.nav.dsop.consumer.ereg.EregConsumer
import no.nav.dsop.consumer.kodeverk.KodeverkConsumer
import no.nav.dsop.consumer.sporingslogg.SporingsloggConsumer
import no.nav.dsop.dto.SporingsloggOutbound

class DsopService(
    private val sporingsloggConsumer: SporingsloggConsumer,
    private val eregConsumer: EregConsumer,
    private val kodeverkConsumer: KodeverkConsumer
) {
    suspend fun getDsop(authToken: String): List<SporingsloggOutbound> {
        return sporingsloggConsumer.getSporingslogg(authToken).map {
            SporingsloggOutbound(
                tema = kodeverkConsumer.getKodeverk(kode = it.tema),
                uthentingsTidspunkt = it.uthentingsTidspunkt,
                mottaker = it.mottaker,
                mottakernavn = eregConsumer.getOrganisasjonsnavn(it.mottaker),
                leverteData = it.leverteData,
                samtykkeToken = it.samtykkeToken
            )
        }
    }
}