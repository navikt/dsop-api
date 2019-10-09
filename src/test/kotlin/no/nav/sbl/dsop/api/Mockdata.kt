package no.nav.sbl.dsop.api

import no.nav.sbl.dsop.api.dto.Sporingslogg

object Mockdata {
    fun getMockdata(): Sporingslogg {
        val sporingslogg = Sporingslogg(
                tema = "Arbeidsavklaringspenger",
                uthentingsTidspunkt = "2018-10-19T12:24:21.675",
                mottaker = "Forsikring AS",
                mottakernavn = "Forsikring AS",
                behandlingsgrunnlag = "Hjemmel")
        return sporingslogg
    }

}