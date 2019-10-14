package no.nav.sbl.dsop.api

import no.nav.sbl.dsop.oppslag.emptyTestEnvironment

fun main(args: Array<String>) {
    Bootstrap.start(webApplication(mockdata = Mockdata.getMockdata(), env = emptyTestEnvironment))
}
