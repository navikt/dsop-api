package no.nav.sbl.dsop.api

data class Environment(
        val dsopApiSporingsloggLesloggerApiKeyUsername: String = System.getenv("DSOP_API_SPORINGSLOGG_LESLOGGER_API_KEY_USERNAME"),
        val dsopApiSporingsloggLesloggerApiKeyPassword: String = System.getenv("DSOP_API_SPORINGSLOGG_LESLOGGER_API_KEY_PASSWORD"),
        val sporingloggLesloggerUrl: String = System.getenv("SPORINGSLOGG_LESLOGGER_URL"),
        val dsopApiKodeverkRestApiApikeyUsername: String = System.getenv("DSOP_API_KODEVERK_REST_API_APIKEY_USERNAME"),
        val dsopApiKodeverkRestApiApikeyPassword: String = System.getenv("DSOP_API_KODEVERK_REST_API_APIKEY_PASSWORD"),
        val kodeverkReestApiUrl: String = System.getenv("KODEVERK_REST_API_URL")
)
