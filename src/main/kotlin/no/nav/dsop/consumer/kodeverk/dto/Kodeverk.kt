package no.nav.dsop.consumer.kodeverk.dto

data class Kodeverk (
        val betydninger: Map<String, List<Betydning>>
)

data class Betydning (
        val beskrivelser: Map<String, Beskrivelse>
)

data class Beskrivelse (
        val tekst: String,
        val term: String
)