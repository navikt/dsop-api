package no.nav.sbl.dsop.api.dto

class Vedtak (
        val vedtakId: String,
        val vedtakstype: String,
        val vedtaksstatus: String,
        val vedtaksperiode: Periode,
        val vedtaksvariant: String?,
        val rettighetstype: String,
        val aktivitetsfase: String,
        val utfall: String
)