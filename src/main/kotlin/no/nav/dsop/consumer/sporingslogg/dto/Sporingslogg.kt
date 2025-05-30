package no.nav.dsop.consumer.sporingslogg.dto

import kotlinx.serialization.Serializable

@Serializable
data class Sporingslogg(
    val tema: String,
    val uthentingsTidspunkt: String? = null,
    val mottaker: String,
    val leverteData: String? = null,
    val samtykkeToken: String? = null
)