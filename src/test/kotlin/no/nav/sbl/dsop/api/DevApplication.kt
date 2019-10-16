package no.nav.sbl.dsop.api

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.client.WireMock.*
import com.github.tomakehurst.wiremock.core.WireMockConfiguration


fun main(args: Array<String>) {
    //Bootstrap.start(webApplication(mockdata = Mockdata.getMockdata(), env = emptyTestEnvironment))
    val server: WireMockServer = WireMockServer(WireMockConfiguration.options().dynamicPort())
    server.start()
    //stubFor(any(anyUrl()).willReturn(okJson("Hoi")))
    io.ktor.server.netty.EngineMain.main(args)
}

