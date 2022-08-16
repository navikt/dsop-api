package no.nav.dsop.config

import io.ktor.client.HttpClient
import no.nav.dsop.consumer.ereg.EregConsumer
import no.nav.dsop.consumer.kodeverk.KodeverkConsumer
import no.nav.dsop.consumer.sporingslogg.SporingsloggConsumer
import no.nav.dsop.service.DsopService

class TestApplicationContext(httpClient: HttpClient) {

    val env = Environment()

    val tokendingsService = DummyTokendingsService()
    val sporingsloggConsumer = SporingsloggConsumer(httpClient, env, tokendingsService)
    val eregConsumer = EregConsumer(httpClient, env)
    val kodeverkConsumer = KodeverkConsumer(httpClient, env)
    val dsopService = DsopService(sporingsloggConsumer, eregConsumer, kodeverkConsumer)
}