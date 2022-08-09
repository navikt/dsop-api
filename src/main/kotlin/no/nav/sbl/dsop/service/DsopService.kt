package no.nav.sbl.dsop.service

import no.nav.sbl.dsop.consumer.ereg.EregConsumer
import no.nav.sbl.dsop.consumer.kodeverk.KodeverkConsumer
import no.nav.sbl.dsop.consumer.sporingslogg.SporingsloggConsumer
import no.nav.sbl.dsop.consumer.sporingslogg.dto.Sporingslogg

class DsopService(
    private val sporingsloggConsumer: SporingsloggConsumer,
    private val eregConsumer: EregConsumer,
    private val kodeverkConsumer: KodeverkConsumer
) {
    suspend fun getDsop(selvbetjeningIdtoken: String): List<Sporingslogg> {

        val sporingslogg = sporingsloggConsumer.getSporingslogg(selvbetjeningIdtoken)

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