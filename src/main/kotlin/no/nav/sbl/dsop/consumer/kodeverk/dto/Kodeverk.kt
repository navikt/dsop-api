package no.nav.sbl.dsop.consumer.kodeverk.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class Kodeverk (
        val betydninger: Map<String, List<Betydning>>
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class Betydning (
        val beskrivelser: Map<String, Beskrivelse>
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class Beskrivelse (
        val tekst: String,
        val term: String
)