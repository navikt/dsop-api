package no.nav.dsop.config

import io.micrometer.prometheusmetrics.PrometheusConfig
import io.micrometer.prometheusmetrics.PrometheusMeterRegistry
import no.nav.dsop.consumer.ereg.EregConsumer
import no.nav.dsop.consumer.kodeverk.KodeverkConsumer
import no.nav.dsop.consumer.sporingslogg.SporingsloggConsumer
import no.nav.dsop.service.DsopService
import no.nav.tms.token.support.azure.exchange.AzureServiceBuilder
import no.nav.tms.token.support.tokendings.exchange.TokendingsServiceBuilder

class ApplicationContext {

    val env = Environment()
    val httpClient = HttpClientBuilder.build()

    val appMicrometerRegistry = PrometheusMeterRegistry(PrometheusConfig.DEFAULT)

    val tokendingsService = TokendingsServiceBuilder.buildTokendingsService()
    val azureService = AzureServiceBuilder.buildAzureService()

    val sporingsloggConsumer = SporingsloggConsumer(httpClient, env, tokendingsService)
    val eregConsumer = EregConsumer(httpClient, env)
    val kodeverkConsumer = KodeverkConsumer(httpClient, env, azureService)
    val dsopService = DsopService(sporingsloggConsumer, eregConsumer, kodeverkConsumer)

}