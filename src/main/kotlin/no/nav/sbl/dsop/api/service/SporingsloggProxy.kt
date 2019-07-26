package no.nav.sbl.dsop.api.service

import no.nav.sbl.dsop.api.dto.Sporingslogg

open class SporingsloggProxy {
    open fun hentSporingslogg(): Sporingslogg {
        return Sporingslogg()
    }
}