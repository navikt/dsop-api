package no.nav.sbl.dsop.config

import no.nav.sbl.dsop.consumer.ereg.EregConsumer
import no.nav.sbl.dsop.consumer.kodeverk.KodeverkConsumer
import no.nav.sbl.dsop.consumer.sporingslogg.SporingsloggConsumer
import no.nav.sbl.dsop.service.DsopService
import no.nav.tms.token.support.tokendings.exchange.TokendingsServiceBuilder

class ApplicationContext {

    val env = Environment()
    val httpClient = HttpClientBuilder.build()

    val tokendingsService = TokendingsServiceBuilder.buildTokendingsService()
    val sporingsloggConsumer = SporingsloggConsumer(httpClient, env, tokendingsService)
    val eregConsumer = EregConsumer(httpClient, env)
    val kodeverkConsumer = KodeverkConsumer(httpClient, env)
    val dsopService = DsopService(sporingsloggConsumer, eregConsumer, kodeverkConsumer)

}