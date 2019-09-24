package no.nav.sbl.dsop.api.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class EregOrganisasjon (
        val navn: Navn? = null
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class Navn(
        val navnelinje1: kotlin.String? = null,
        val navnelinje2: kotlin.String? = null,
        val navnelinje3: kotlin.String? = null,
        val navnelinje4: kotlin.String? = null,
        val navnelinje5: kotlin.String? = null
)