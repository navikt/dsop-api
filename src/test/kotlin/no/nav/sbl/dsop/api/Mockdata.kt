package no.nav.sbl.dsop.api

import no.nav.sbl.dsop.api.dto.LeverteData
import no.nav.sbl.dsop.api.dto.Periode
import no.nav.sbl.dsop.api.dto.Sporingslogg
import no.nav.sbl.dsop.api.dto.Vedtak
import java.time.LocalDate
import java.time.LocalDateTime

object Mockdata {
    fun getMockdata(): Sporingslogg {
        val vedtaksListe = mutableListOf<Vedtak>()
        vedtaksListe.add(Vedtak(
                vedtakId = "1",
                vedtakstype = "Endring",
                vedtaksstatus = "Iverksatt",
                vedtaksperiode = Periode(fra = LocalDate.now(), til = LocalDate.now()),
                vedtaksvariant = "",
                rettighetstype = "Arbeidsavklaringspenger",
                aktivitetsfase = "Under arbeidsavklaring",
                utfall = "Ja"
        ))
        vedtaksListe.add(Vedtak(
                vedtakId = "2",
                vedtakstype = "Endring",
                vedtaksstatus = "Iverksatt",
                vedtaksperiode = Periode(fra = LocalDate.now(), til = LocalDate.now()),
                vedtaksvariant = "",
                rettighetstype = "Arbeidsavklaringspenger",
                aktivitetsfase = "Under arbeidsavklaring",
                utfall = "Ja"
        ))
        val leverteData = LeverteData(uttrekksperiode = Periode(fra = LocalDate.now(), til = LocalDate.now()), vedtak = vedtaksListe)
        val sporingslogg = Sporingslogg(
                tema = "Arbeidsavklaringspenger",
                uthentingsTidspunkt = LocalDateTime.now(),
                mottaker = "Forsikring AS",
                behandlingsgrunnlag = "Hjemmel",
                leverteData = leverteData)
        return sporingslogg
    }

}