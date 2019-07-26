package no.nav.sbl.dsop.api.dto

import java.time.LocalDateTime

data class Sporingslogg (
        val tema: String? = null,
        val uthentingsTidspunkt: LocalDateTime? = null,
        val mottaker: String? = null,
        val behandlingsgrunnlag: String? = null,
        val leverteData: LeverteData? = null
)