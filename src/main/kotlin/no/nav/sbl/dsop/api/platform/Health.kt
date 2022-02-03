package no.nav.sbl.dsop.api.admin.platform

import io.ktor.application.call
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.response.respondText
import io.ktor.response.respondTextWriter
import io.ktor.routing.Routing
import io.ktor.routing.get
import io.ktor.routing.route
import io.prometheus.client.CollectorRegistry
import io.prometheus.client.exporter.common.TextFormat
import io.prometheus.client.hotspot.DefaultExports


fun Routing.health(
    ready: () -> Boolean = { true },
    alive: () -> Boolean = { true },
    collectorRegistry: CollectorRegistry = CollectorRegistry.defaultRegistry
) {

    DefaultExports.initialize()

    fun statusFor(b: () -> Boolean) = b().let { if (it) HttpStatusCode.OK else HttpStatusCode.InternalServerError }

    route("/internal") {

        get("/isReady") {
            statusFor(ready).let { call.respondText("Ready: $it", status = it) }
        }

        get("/isAlive") {
            statusFor(alive).let { call.respondText("Alive: $it", status = it) }
        }

        get("/prometheus") {
            val names = call.request.queryParameters.getAll("name")?.toSet() ?: setOf()
            call.respondTextWriter(ContentType.parse(TextFormat.CONTENT_TYPE_004)) {
                TextFormat.write004(this, collectorRegistry.filteredMetricFamilySamples(names))
                flush()
            }
        }
    }

}

