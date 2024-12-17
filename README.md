# DSOP-Api

![Deploy-to-prod](https://github.com/navikt/dsop-api/workflows/Deploy-to-prod/badge.svg) | ![Deploy-to-dev](https://github.com/navikt/dsop-api/workflows/Deploy-to-dev/badge.svg)

Ktor-backend som skal gi brukeren innsikt i informasjonen NAV har lagret.

Secrets ligger i [Nais console](https://console.nav.cloud.nais.io/team/personbruker/secrets).

## Lokal kjøring

Kjør main-funksjon i [localServer](src/test/kotlin/no/nav/dsop/localServer.kt). Endepunkter eksponeres da på localhost:8080, og kan kalles uten token. Merk at alle eksterne avhengigheter er mocket ut.

Appen kan kjøres opp i development mode med auto-reload dersom man legger til `-Dio.ktor.development=true` under VM options under Run/Debug configurations i IntelliJ. Merk at IntelliJ ikke bygger prosjektet automatisk ved endringer, så man må fortsatt bygge manuelt for at endringer skal registreres.

## Deploy til dev

[Actions](https://github.com/navikt/dsop-api/actions) -> Velg workflow -> Run workflow -> Velg branch -> Run workflow

## Prodsetting

-   Lag en PR til master, og merge inn etter godkjenning
-   Lag en release på master med versjon-bump, beskrivende tittel og oppsummering av endringene dine
-   Publiser release'en for å starte deploy til prod

## Logging

[Kibana](https://logs.adeo.no/app/discover#/view/f3fcf000-278f-11ed-9b1a-4723a5e7a9db)

## Henvendelser

Spørsmål knyttet til koden eller prosjektet kan rettes mot https://github.com/orgs/navikt/teams/personbruker

## For NAV-ansatte

Interne henvendelser kan sendes via Slack i kanalen #team-personbruker.
