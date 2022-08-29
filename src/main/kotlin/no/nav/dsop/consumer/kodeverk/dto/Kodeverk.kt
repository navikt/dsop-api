package no.nav.dsop.consumer.kodeverk.dto

import kotlinx.serialization.Serializable

@Serializable
data class Kodeverk(
    val betydninger: Map<String, List<Betydning>>
)

@Serializable
data class Betydning(
    val beskrivelser: Map<String, Beskrivelse>
)

@Serializable
data class Beskrivelse(
    val tekst: String,
    val term: String
)