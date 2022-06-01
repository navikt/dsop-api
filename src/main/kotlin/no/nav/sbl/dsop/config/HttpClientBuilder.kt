package no.nav.sbl.dsop.config

import io.ktor.client.HttpClient
import io.ktor.client.features.defaultRequest
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.request.header
import no.nav.common.log.MDCConstants
import org.slf4j.MDC

object HttpClientBuilder {

    fun build(): HttpClient {
        return HttpClient {
            defaultRequest {
                header("Nav-Call-Id", MDC.get(MDCConstants.MDC_CALL_ID))
                header("Nav-Consumer-Id", CONSUMER_ID)
            }
            install(JsonFeature)
            expectSuccess = false
        }
    }

}
