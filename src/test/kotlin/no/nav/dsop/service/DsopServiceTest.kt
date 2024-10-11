package no.nav.dsop.service

import io.kotest.assertions.assertSoftly
import io.kotest.matchers.shouldBe
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

class DsopServiceTest {

    private val eregConsumer: EregConsumer = mockk()
    private val kodeverkConsumer: KodeverkConsumer = mockk()
    private val sporingsloggConsumer: SporingsloggConsumer = mockk()

    private val dsopService = DsopService(sporingsloggConsumer, eregConsumer, kodeverkConsumer)

    @Test
    fun `skal returnere sporingslogg`(): Unit = runBlocking {
        coEvery { eregConsumer.getOrganisasjonsnavn(any()) } returns ORGNAVN
        coEvery { kodeverkConsumer.getKodeverk(any()) } returns TEMA
        coEvery { sporingsloggConsumer.getSporingslogg(any()) } returns singletonList(buildSporingslogg())

        val sporingsloggList = dsopService.getDsop("")
        assertEquals(1, sporingsloggList.size)

        assertSoftly(sporingsloggList[0]) {
            tema shouldBe TEMA
            uthentingsTidspunkt shouldBe UTHENTINGS_TIDSPUNKT
            mottaker shouldBe MOTTAKER
            mottakernavn shouldBe ORGNAVN
            leverteData shouldBe LEVERTE_DATA
            samtykkeToken shouldBe SAMTYKKE_TOKEN
        }
    }

    private fun buildSporingslogg(): Sporingslogg {
        return Sporingslogg(
            tema = TEMA_KODE,
            mottaker = MOTTAKER,
            uthentingsTidspunkt = UTHENTINGS_TIDSPUNKT,
            mottakernavn = MOTTAKERNAVN,
            leverteData = LEVERTE_DATA,
            samtykkeToken = SAMTYKKE_TOKEN
        )
    }

    companion object {
        private const val ORGNAVN = "orgnavn"
        private const val TEMA = "tema"
        private const val TEMA_KODE = "temaKode"
        private const val UTHENTINGS_TIDSPUNKT = "uthentingsTidspunkt"
        private const val MOTTAKER = "mottaker"
        private const val MOTTAKERNAVN = "mottakernavn"
        private const val LEVERTE_DATA = "leverteData"
        private const val SAMTYKKE_TOKEN = "samtykkeToken"
    }
}