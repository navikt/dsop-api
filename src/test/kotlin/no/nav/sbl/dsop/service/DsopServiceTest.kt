package no.nav.sbl.dsop.service

import io.mockk.coEvery
import io.mockk.mockk
import no.nav.sbl.dsop.consumer.ereg.EregConsumer
import no.nav.sbl.dsop.consumer.kodeverk.KodeverkConsumer
import no.nav.sbl.dsop.consumer.sporingslogg.SporingsloggConsumer
import no.nav.sbl.dsop.consumer.sporingslogg.dto.Sporingslogg
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.util.Collections.singletonList

internal class DsopServiceTest {

    private val orgnavn = "orgnavn"
    private val tema = "tema"
    private val temaKode = "temaKode"
    private val uthentingsTidspunkt = "uthentingsTidspunkt"
    private val mottaker = "mottaker"
    private val mottakernavn = "mottakernavn"
    private val behandlingsgrunnlag = "behandlingsgrunnlag"
    private val leverteData = "leverteData"
    private val samtykkeToken = "samtykkeToken"

    private val eregConsumer: EregConsumer = mockk()
    private val kodeverkConsumer: KodeverkConsumer = mockk()

    private val sporingsloggConsumer: SporingsloggConsumer = mockk()

    private val dsopService = DsopService(sporingsloggConsumer, eregConsumer, kodeverkConsumer)

    @Test
    suspend fun `skal returnere sporingslogg`() {
        coEvery { eregConsumer.getOrganisasjonsnavn(any()) } returns orgnavn
        coEvery { kodeverkConsumer.getKodeverk(any()) } returns tema
        coEvery { sporingsloggConsumer.getSporingslogg(any()) } returns singletonList(buildSporingslogg())

        val sporingsloggList = dsopService.getDsop("")
        assertEquals(1, sporingsloggList.size)

        val sporingslogg = sporingsloggList[0]

        assertThat(sporingslogg.tema).isEqualTo(tema)
        assertThat(sporingslogg.uthentingsTidspunkt).isEqualTo(uthentingsTidspunkt)
        assertThat(sporingslogg.mottaker).isEqualTo(mottaker)
        assertThat(sporingslogg.mottakernavn).isEqualTo(orgnavn)
        assertThat(sporingslogg.leverteData).isEqualTo(leverteData)
        assertThat(sporingslogg.samtykkeToken).isEqualTo(samtykkeToken)
    }

    private fun buildSporingslogg(): Sporingslogg {
        return Sporingslogg(
            tema = temaKode,
            mottaker = mottaker,
            uthentingsTidspunkt = uthentingsTidspunkt,
            mottakernavn = mottakernavn,
            behandlingsgrunnlag = behandlingsgrunnlag,
            leverteData = leverteData,
            samtykkeToken = samtykkeToken
        )
    }
}