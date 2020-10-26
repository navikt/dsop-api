package no.nav.sbl.dsop.api

data class Environment(
        val apiKeyUsername: String = System.getenv("API_KEY_USERNAME") ?: "",
        val dsopApiSporingsloggLesloggerApiKeyPassword: String = System.getenv("DSOP_API_SPORINGSLOGG_LESLOGGER_API_KEY_PASSWORD") ?: "",
        val sporingloggLesloggerUrl: String = System.getenv("SPORINGSLOGG_LESLOGGER_URL") ?: "http://localhost:8090/sporingslogg/",
        val dsopApiKodeverkRestApiApikeyPassword: String = System.getenv("DSOP_API_KODEVERK_REST_API_APIKEY_PASSWORD") ?: "",
        val kodeverkRestApiUrl: String = System.getenv("KODEVERK_REST_API_URL") ?: "http://localhost:8090/kodeverk/",
        val dsopApiEregApiApikeyPassword: String = System.getenv("DSOP_API_EREG_API_APIKEY_PASSWORD") ?: "",
        val eregApiUrl: String = System.getenv("EREG_API_URL") ?: "http://localhost:8090/ereg/",
        val securityJwksIssuer: String = "loginservice",
        val securityJwksUrl: String = System.getenv("LOGINSERVICE_IDPORTEN_DISCOVERY_URL") ?: "https://dummyUrl.com",
        val securityAudience: String = System.getenv("LOGINSERVICE_IDPORTEN_AUDIENCE") ?: "dummyAudience"
) {
    open fun isMockedEnvironment(): Boolean {
        return apiKeyUsername.isNullOrEmpty()
    }
}
