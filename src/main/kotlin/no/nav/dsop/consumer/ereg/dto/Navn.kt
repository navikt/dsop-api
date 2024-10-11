package no.nav.dsop.consumer.ereg.dto

import kotlinx.serialization.Serializable

@Serializable
data class Navn(
    val navnelinje1: String? = null,
    val navnelinje2: String? = null,
    val navnelinje3: String? = null,
    val navnelinje4: String? = null,
    val navnelinje5: String? = null
) {
    fun joinedToString(): String {
        return listOfNotNull(navnelinje1, navnelinje2, navnelinje3, navnelinje4, navnelinje5).joinToString(" ")
    }
}