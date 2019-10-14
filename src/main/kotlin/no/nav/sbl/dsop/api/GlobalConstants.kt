package no.nav.sbl.dsop.api

val CONSUMER_ID = "dsop-api"
val HTTP_STATUS_CODES_2XX = IntRange(200, 299)
val KODEVERK_TEMA_CACHE = HashMap<String, String>()
val KODEVERK_TEMA_CACHE_CLEARING_INTERVAL: Long = 24 * 60 * 60 * 1000
val OIDC_COOKIE_NAME = "selvbetjening-idtoken"
