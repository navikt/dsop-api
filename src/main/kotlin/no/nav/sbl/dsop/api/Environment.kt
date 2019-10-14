package no.nav.sbl.dsop.api

import java.net.URL

data class Environment(
        val apiKeyUsername: String = System.getenv("API_KEY_USERNAME"),
        val dsopApiSporingsloggLesloggerApiKeyPassword: String = System.getenv("DSOP_API_SPORINGSLOGG_LESLOGGER_API_KEY_PASSWORD"),
        val sporingloggLesloggerUrl: String = System.getenv("SPORINGSLOGG_LESLOGGER_URL"),
        val dsopApiKodeverkRestApiApikeyPassword: String = System.getenv("DSOP_API_KODEVERK_REST_API_APIKEY_PASSWORD"),
        val kodeverkRestApiUrl: String = System.getenv("KODEVERK_REST_API_URL"),
        val dsopApiEregApiApikeyPassword: String = System.getenv("DSOP_API_EREG_API_APIKEY_PASSWORD"),
        val eregApiUrl: String = System.getenv("EREG_API_URL"),
        val securityAudience: String = System.getenv("AUDIENCE") ?: "dummyAudience",
        val securityJwksIssuer: String = System.getenv("JWKS_ISSUER") ?: "dummyIssuer",
        val securityJwksUri: URL = URL(System.getenv("JWKS_URI") ?: "https://dummyUrl.com"),
        val securityJwksUrl: String = System.getenv("JWKS_DISCOVERY_URL") ?: "https://dummyUrl.com",
        val useMockData: Boolean = false
)
