package no.nav.sbl.dsop.api

fun main(args: Array<String>) {
    Bootstrap.start(webApplication(mockdata = Mockdata.getMockdata()))
}
