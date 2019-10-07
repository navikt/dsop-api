package no.nav.personbruker.dittnav.api.config

import com.auth0.jwk.JwkProvider
import com.auth0.jwk.JwkProviderBuilder
import io.ktor.auth.jwt.JWTAuthenticationProvider
import io.ktor.auth.jwt.JWTCredential
import io.ktor.auth.jwt.JWTPrincipal
import mu.KLogging
import no.nav.sbl.dsop.api.Environment
import java.net.URL
import java.util.concurrent.TimeUnit

fun JWTAuthenticationProvider.Configuration.setupOidcAuthentication(environment: Environment) {
    KLogging().logger.info("Executing setupOidcAuthentication...")
    val jwkProvider = Security.initJwkProvider(environment.securityJwksUri)
    verifier(jwkProvider, environment.securityJwksIssuer)
    realm = "dsop-api"
    validate { credentials ->
        //return@validate Security.validationLogicPerRequest(credentials, environment)
        return@validate Security.validationLogicPerRequest(credentials, environment)
    }
    skipWhen { call -> true
    }
}

object Security {
    fun initJwkProvider(securityJwksUri: URL): JwkProvider {
        return JwkProviderBuilder(securityJwksUri)
            .cached(10, 24, TimeUnit.HOURS)
            .rateLimited(10, 1, TimeUnit.MINUTES)
            .build()
    }

    fun validationLogicPerRequest(credentials: JWTCredential, environment: Environment): JWTPrincipal? {
        KLogging().logger.info("Executing validationLogicPerRequest...")
        return when (isCorrectAudienceSet(credentials, environment)) {
            true -> JWTPrincipal(credentials.payload)
            false -> null
        }
    }

    private fun isCorrectAudienceSet(credentials: JWTCredential, environment: Environment) =
            credentials.payload.audience.contains(environment.securityAudience)

}
