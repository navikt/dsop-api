package no.nav.dsop

import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.mockk.coEvery
import io.mockk.mockkStatic
import no.nav.dsop.config.TestApplicationContext
import no.nav.dsop.config.mock.setupMockedClient
import no.nav.dsop.config.testModule
import no.nav.dsop.utils.getAuthTokenFromRequest

fun main() {
    embeddedServer(Netty, port = 8080, watchPaths = listOf("classes")) {
        mockkStatic(::getAuthTokenFromRequest)
        coEvery { getAuthTokenFromRequest(any()) } returns "dummyToken"

        testModule(TestApplicationContext(setupMockedClient()))
    }.start(wait = true)
}