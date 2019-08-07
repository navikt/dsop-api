[![CircleCI](https://circleci.com/gh/navikt/dsop-api.svg?style=svg)](https://circleci.com/gh/navikt/dsop-api)
[![Maintainability](https://api.codeclimate.com/v1/badges/1a17b576c58daeb8bbc0/maintainability)](https://codeclimate.com/github/navikt/dsop-api/maintainability)
[![Test Coverage](https://api.codeclimate.com/v1/badges/1a17b576c58daeb8bbc0/test_coverage)](https://codeclimate.com/github/navikt/dsop-api/test_coverage)

# DSOP-Api

React applikasjon som skal gi brukeren innsikt i informasjonen NAV har lagret.

## Komme i gang

Hent repoet fra github

```
git clone https://github.com/navikt/dsop-api.git
```

Installer nødvendige pakker:

```
npm install
```

Start applikasjonen lokalt:

```
npm start
```

## Feature toggles

DSOP-Api benytter Unleash til å skru av og på funksjonalitet som er under utvikling.<br>
https://unleash.nais.adeo.no/#/features<br>
Obs: Unleash er kun tilgjengelig i fagsystemsonen.

## Deployering

Applikasjonen bygges automatisk til dev / https://www-q0.nav.no/person/dsop-api ved merge til master eller ved manuell godkjenning i [CircleCI](https://circleci.com/gh/navikt/workflows/dsop-api). <br><br>
For å lansere applikasjonen til produksjon / https://www.nav.no/person/dsop-api, benytt [npm version](https://docs.npmjs.com/cli/version) til å oppdatere package.json og lage samsvarende Git-tag. Eks:

```
npm version patch -m "Din melding"
```

Push deretter den nye versjonen til GitHub og merge til master.

```
git push && git push --tags
```

Godkjenn produksjonssettingen i [CircleCI](https://circleci.com/gh/navikt/workflows/dsop-api).

## Logging

Feil ved API-kall blir logget via frontendlogger og vises i Kibana<br>
[https://logs.adeo.no](https://logs.adeo.no/app/kibana#/discover/ad01c200-4af4-11e9-a5a6-7fddb220bd0c)

## Henvendelser

Spørsmål knyttet til koden eller prosjektet kan rettes mot https://github.com/orgs/navikt/teams/personbruker

## For NAV-ansatte

Interne henvendelser kan sendes via Slack i kanalen #team-personbruker.