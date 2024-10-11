package no.nav.dsop.dto

import kotlinx.serialization.Serializable

@Serializable
data class SporingsloggOutbound(
    val tema: String,
    val uthentingsTidspunkt: String? = null,
    val mottaker: String,
    val mottakernavn: String? = null,
    val leverteData: String? = null,
    val samtykkeToken: String? = null
)