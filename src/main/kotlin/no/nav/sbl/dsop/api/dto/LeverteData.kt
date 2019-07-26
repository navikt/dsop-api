package no.nav.sbl.dsop.api.dto

import java.time.LocalDate

data class LeverteData (
        val uttrekksperiode: Periode,
        val vedtak: List<Vedtak>
)