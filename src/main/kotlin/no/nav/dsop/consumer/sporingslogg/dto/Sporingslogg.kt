package no.nav.dsop.consumer.sporingslogg.dto

import kotlinx.serialization.Serializable

@Serializable
data class Sporingslogg(
    val tema: String,
    val uthentingsTidspunkt: String? = null,
    val mottaker: String,
    val mottakernavn: String? = null,
    val behandlingsgrunnlag: String? = null, //todo: fjern
    val leverteData: String? = null,
    val samtykkeToken: String? = null
)

// også todo: Lage separat klasse for returnert objekt