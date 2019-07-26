package no.nav.sbl.dsop.api

import no.nav.sbl.dsop.api.dto.Sporingslogg
import no.nav.sbl.dsop.api.service.SporingsloggProxy

fun main(args: Array<String>) {
    Bootstrap.start(
            webApplication(
                    proxy = object : SporingsloggProxy() { override fun hentSporingslogg(): Sporingslogg {return MockData.getMockData()}}
            )
    )
}