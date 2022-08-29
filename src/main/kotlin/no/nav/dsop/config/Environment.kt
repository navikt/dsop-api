package no.nav.dsop.config

data class Environment(
    val corsAllowedOrigins: String = System.getenv("CORS_ALLOWED_ORIGINS"),
    val corsAllowedSchemes: String = System.getenv("CORS_ALLOWED_SCHEMES"),

    val sporingloggLesloggerUrl: String = System.getenv("SPORINGSLOGG_LESLOGGER_URL"),
    val kodeverkRestApiUrl: String = System.getenv("KODEVERK_REST_API_URL"),
    val eregApiUrl: String = System.getenv("EREG_API_URL"),

    val sporingsloggTargetApp: String = System.getenv("SPORINGSLOGG_TARGET_APP"),
)