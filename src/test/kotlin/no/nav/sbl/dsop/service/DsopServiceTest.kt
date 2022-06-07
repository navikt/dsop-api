package no.nav.sbl.dsop.service

import io.mockk.coEvery
import io.mockk.mockk
import no.nav.sbl.dsop.consumer.ereg.EregConsumer
import no.nav.sbl.dsop.consumer.kodeverk.KodeverkConsumer
import no.nav.sbl.dsop.consumer.sporingslogg.SporingsloggConsumer
import no.nav.sbl.dsop.consumer.sporingslogg.dto.Sporingslogg
import org.junit.jupiter.api.Test
import java.util.Collections.singletonList
import kotlin.test.assertEquals

internal class DsopServiceTest {

    private val eregConsumer: EregConsumer = mockk()
    private val kodeverkConsumer: KodeverkConsumer = mockk()
    private val sporingsloggConsumer: SporingsloggConsumer = mockk()

    private val dsopService = DsopService(sporingsloggConsumer, eregConsumer, kodeverkConsumer)


    // TODO: Skrive flere tester etter cache-endringer
    @Test
    fun `skall til enhetstest`() {
        coEvery { eregConsumer.getOrganisasjonsnavn(any(), any(), any()) } returns ""
        coEvery { kodeverkConsumer.getKodeverk(any()) } returns ""
        coEvery { sporingsloggConsumer.getSporingslogg(any(), any()) } returns singletonList(
            Sporingslogg(
                tema = "",
                mottaker = ""
            )
        )

        val sporingslogg = dsopService.getDsop("", "")
        assertEquals(1, sporingslogg.size)
    }
}