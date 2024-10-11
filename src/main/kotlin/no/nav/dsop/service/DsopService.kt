package no.nav.dsop.service

import no.nav.dsop.consumer.ereg.EregConsumer
import no.nav.dsop.consumer.kodeverk.KodeverkConsumer
import no.nav.dsop.consumer.sporingslogg.SporingsloggConsumer
import no.nav.dsop.consumer.sporingslogg.dto.Sporingslogg

class DsopService(
    private val sporingsloggConsumer: SporingsloggConsumer,
    private val eregConsumer: EregConsumer,
    private val kodeverkConsumer: KodeverkConsumer
) {
    suspend fun getDsop(authToken: String): List<Sporingslogg> {

        val sporingslogg = sporingsloggConsumer.getSporingslogg(authToken)

        // todo: bruk copy her
        return sporingslogg.map {
            Sporingslogg(
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