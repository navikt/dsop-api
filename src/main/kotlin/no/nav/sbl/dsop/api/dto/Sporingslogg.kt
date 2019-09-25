package no.nav.sbl.dsop.api.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.time.LocalDate
import java.time.LocalDateTime

data class Sporingslogg (
        val tema: String? = null,
        val uthentingsTidspunkt: LocalDateTime? = null,
        val mottaker: String? = null,
        val behandlingsgrunnlag: String? = null,
        val leverteData: LeverteData? = null
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class Sporingslogg2 (
        val tema: String? = null,
//        val uthentingsTidspunkt: LocalDateTime? = null,
        val uthentingsTidspunkt: String? = null,
        val mottaker: String? = null,
        val mottakernavn: String? = null,
        val behandlingsgrunnlag: String? = null,
        val leverteData: String? = null,
        val samtykkeToken: String? = null
)

data class LeverteData (
        val uttrekksperiode: Periode,
        val vedtak: List<Vedtak>
)

class Vedtak (
        val vedtakId: String,
        val vedtakstype: String,
        val vedtaksstatus: String,
        val vedtaksperiode: Periode,
        val vedtaksvariant: String?,
        val rettighetstype: String,
        val aktivitetsfase: String,
        val utfall: String
)

data class Periode (
        val fra: LocalDate,
        val til: LocalDate
)