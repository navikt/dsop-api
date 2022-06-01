package no.nav.sbl.dsop.service

import no.nav.sbl.dsop.config.KODEVERK_TEMA_CACHE
import no.nav.sbl.dsop.consumer.dsop.DsopConsumer
import no.nav.sbl.dsop.consumer.dsop.dto.Sporingslogg
import no.nav.sbl.dsop.consumer.ereg.EregConsumer
import no.nav.sbl.dsop.consumer.kodeverk.KodeverkConsumer

class DsopService(
    private val dsopConsumer: DsopConsumer,
    private val eregConsumer: EregConsumer,
    private val kodeverkConsumer: KodeverkConsumer
) {
    fun getDsop(authorization: String, selvbetjeningIdtoken: String): List<Sporingslogg> {

        val sporingslogg = dsopConsumer.getSporingslogg(authorization, selvbetjeningIdtoken)

        val orgnavnCache = HashMap<String, String>()

        return sporingslogg.map {
            if (orgnavnCache[it.mottaker] == null) {
                orgnavnCache[it.mottaker] =
                    eregConsumer.getOrganisasjonsnavn(
                        selvbetjeningstoken = selvbetjeningIdtoken,
                        authorization = authorization,
                        orgnr = it.mottaker,
                    )
            }
            if (KODEVERK_TEMA_CACHE[it.tema] == null) {
                KODEVERK_TEMA_CACHE[it.tema] =
                    kodeverkConsumer.getKodeverk(kode = it.tema)
            }

            Sporingslogg(
                tema = KODEVERK_TEMA_CACHE[it.tema] ?: it.tema,
                uthentingsTidspunkt = it.uthentingsTidspunkt,
                mottaker = it.mottaker,
                mottakernavn = orgnavnCache[it.mottaker],
                leverteData = it.leverteData,
                samtykkeToken = it.samtykkeToken
            )

        }
    }
}