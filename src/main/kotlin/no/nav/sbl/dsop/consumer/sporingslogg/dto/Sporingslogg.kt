package no.nav.sbl.dsop.consumer.sporingslogg.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class Sporingslogg (
        val tema: String,
        val uthentingsTidspunkt: String? = null,
        val mottaker: String,
        val mottakernavn: String? = null,
        val behandlingsgrunnlag: String? = null,
        val leverteData: String? = null,
        val samtykkeToken: String? = null
)