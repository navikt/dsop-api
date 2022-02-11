package no.nav.sbl.dsop.api

data class Environment(
    val corsAllowedOrigins: String = System.getenv("CORS_ALLOWED_ORIGINS") ?: "",
    val corsAllowedSchemes: String = System.getenv("CORS_ALLOWED_SCHEMES") ?: "https",
    val sporingloggLesloggerUrl: String = System.getenv("SPORINGSLOGG_LESLOGGER_URL")
        ?: "http://localhost:8090/sporingslogg/",
    val kodeverkRestApiUrl: String = System.getenv("KODEVERK_REST_API_URL") ?: "http://localhost:8090/kodeverk/",
    val eregApiUrl: String = System.getenv("EREG_API_URL") ?: "http://localhost:8090/ereg/",
    val securityJwksIssuer: String = "loginservice",
    val securityJwksUrl: String = System.getenv("LOGINSERVICE_IDPORTEN_DISCOVERY_URL") ?: "https://dummyUrl.com",
    val securityAudience: String = System.getenv("LOGINSERVICE_IDPORTEN_AUDIENCE") ?: "dummyAudience",
    val personopplysningerProxyTargetApp: String = System.getenv("PERSONOPPLYSNINGER_PROXY_TARGET_APP") ?: "",
) {
    fun isMockedEnvironment(): Boolean {
        return corsAllowedOrigins.isEmpty()
    }
}
