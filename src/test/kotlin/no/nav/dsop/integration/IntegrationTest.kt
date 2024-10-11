package no.nav.dsop.integration

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.statement.HttpResponse
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.config.ApplicationConfig
import io.ktor.server.testing.ApplicationTestBuilder
import io.ktor.server.testing.testApplication
import no.nav.dsop.config.TestApplicationContext
import no.nav.dsop.config.testModule

open class IntegrationTest {

    fun integrationTest(httpClient: HttpClient, block: suspend ApplicationTestBuilder.() -> Unit) = testApplication {
        environment {
            config = ApplicationConfig("application-test.conf")
        }
        application {
            testModule(TestApplicationContext(httpClient))
        }
        block()
    }

    suspend fun ApplicationTestBuilder.get(path: String): HttpResponse {
        val token = createAccessToken()

        return httpClient.get(path) {
            header("Authorization", "Bearer $token")
        }
    }

    private fun createAccessToken(fnr: String = "12341234123"): String {
        return JWT.create().withClaim("pid", fnr).sign(Algorithm.HMAC256("1"))
    }

    private val ApplicationTestBuilder.httpClient
        get() = createClient {
            install(ContentNegotiation) { json() }
        }
}