package no.nav.sbl.dsop.api

import no.nav.sbl.dsop.api.dto.Sporingslogg

fun main(args: Array<String>) {
    Bootstrap.start(webApplication(mockdata = Mockdata.getMockdata()))
}
