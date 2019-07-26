package no.nav.sbl.dsop.api.dto

import java.time.LocalDate

data class Periode (
        val fra: LocalDate,
        val til: LocalDate
)