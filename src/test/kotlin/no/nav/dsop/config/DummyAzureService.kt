package no.nav.dsop.config

import no.nav.tms.token.support.azure.exchange.AzureService

class DummyAzureService : AzureService {
    override suspend fun getAccessToken(targetApp: String) = "dummy azure token"
}