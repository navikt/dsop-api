package no.nav.sbl.dsop.api

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock.*
import com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig
import no.nav.sbl.dsop.api.dto.Sporingslogg
import java.io.InputStreamReader


fun main(args: Array<String>) {
    val server = WireMockServer(wireMockConfig().port(8090))
    server.start()
    configureFor(server.port())
    stubFor(any(urlPathEqualTo("/sporingslogg/")).willReturn(okJson(Testdata.sporingsloggJson())))
    stubFor(any(urlMatching("/ereg/.*")).willReturn(okJson(Testdata.eregJson())))
    stubFor(any(urlMatching("/kodeverk/.*")).willReturn(okJson(Testdata.kodeverkJson())))
    io.ktor.server.netty.EngineMain.main(args)
}

object Testdata {
    fun sporingsloggJson(): String {
        val sporingslogger = ArrayList<Sporingslogg>()
        sporingslogger.add(Sporingslogg(
                tema = "AAP",
                uthentingsTidspunkt = "2018-10-19T12:24:21.675",
                mottaker = "991003525",
                behandlingsgrunnlag = "Hjemmel"))
        sporingslogger.add(Sporingslogg(
                tema = "AAP",
                uthentingsTidspunkt = "2019-02-04T11:33:21.675",
                mottaker = "991003525",
                behandlingsgrunnlag = "Hjemmel"))
        return ObjectMapper().writeValueAsString(sporingslogger)
    }

    fun eregJson(): String {
        return InputStreamReader(this.javaClass.getResourceAsStream("/ereg-organisasjon-991003525.json")).readText()
    }

    fun kodeverkJson(): String {
        return InputStreamReader(this.javaClass.getResourceAsStream("/kodeverk-tema.json")).readText()
    }
}

