package no.nav.dsop.service

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import no.nav.dsop.consumer.ereg.EregConsumer
import no.nav.dsop.consumer.kodeverk.KodeverkConsumer
import no.nav.dsop.consumer.sporingslogg.SporingsloggConsumer
import no.nav.dsop.consumer.sporingslogg.dto.Sporingslogg
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
    fun `skal returnere sporingslogg`(): Unit = runBlocking {
        coEvery { eregConsumer.getOrganisasjonsnavn(any()) } returns orgnavn
        coEvery { kodeverkConsumer.getKodeverk(any()) } returns tema
        coEvery { sporingsloggConsumer.getSporingslogg(any()) } returns singletonList(buildSporingslogg())

        val sporingsloggList = dsopService.getDsop("")
        assertEquals(1, sporingsloggList.size)

        val sporingslogg = sporingsloggList[0]

        assertEquals(tema, sporingslogg.tema)
        assertEquals(uthentingsTidspunkt, sporingslogg.uthentingsTidspunkt)
        assertEquals(mottaker, sporingslogg.mottaker)
        assertEquals(orgnavn, sporingslogg.mottakernavn)
        assertEquals(leverteData, sporingslogg.leverteData)
        assertEquals(samtykkeToken, sporingslogg.samtykkeToken)
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