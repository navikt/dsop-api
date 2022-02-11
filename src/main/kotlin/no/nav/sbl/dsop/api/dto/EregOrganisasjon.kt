package no.nav.sbl.dsop.api.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class EregOrganisasjon (
        val navn: Navn
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class Navn(
        val navnelinje1: String? = null,
        val navnelinje2: String? = null,
        val navnelinje3: String? = null,
        val navnelinje4: String? = null,
        val navnelinje5: String? = null
) {
    fun getNavn(): String {
        var navn = navnelinje1 ?: ""
        if (!navnelinje2.isNullOrEmpty()) navn = navn.plus(" $navnelinje2")
        if (!navnelinje3.isNullOrEmpty()) navn = navn.plus(" $navnelinje3")
        if (!navnelinje4.isNullOrEmpty()) navn = navn.plus(" $navnelinje4")
        if (!navnelinje5.isNullOrEmpty()) navn = navn.plus(" $navnelinje5")
        return navn
    }
}