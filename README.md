[![CircleCI](https://circleci.com/gh/navikt/dsop-api.svg?style=svg)](https://circleci.com/gh/navikt/dsop-api)

# DSOP-Api

Api bygget på Ktor for å koble seg opp mot DSOP / sporingslogg 

## Deployering

Applikasjonen bygges automatisk til dev / https://www-q0.nav.no/person/dsop-api ved merge til master eller ved manuell godkjenning i [CircleCI](https://circleci.com/gh/navikt/workflows/dsop-api). <br><br>
For å lansere applikasjonen til produksjon / https://www.nav.no/person/personopplysninger-api, knytt en commit til en [Git tag](https://git-scm.com/book/en/v2/Git-Basics-Tagging):

```
git tag -a vX.X.X -m "Din melding"
```

Push den nye versjonen til GitHub og merge til master.

```
git push --tags
```

Godkjenn produksjonssettingen i [CircleCI](https://circleci.com/gh/navikt/workflows/dsop-api).

## Lokal Kjøring

For å kjøre opp løsningen lokalt, kjør [DevBootstrap](https://github.com/navikt/dsop-api/blob/master/src/test/kotlin/no/nav/sbl/dsop/api/DevBootstrap.kt).

## Logging

Feil ved API-kall blir logget via frontendlogger og vises i Kibana<br>
[https://logs.adeo.no](https://logs.adeo.no/app/kibana#/discover/ad01c200-4af4-11e9-a5a6-7fddb220bd0c)

## Henvendelser

Spørsmål knyttet til koden eller prosjektet kan rettes mot https://github.com/orgs/navikt/teams/personbruker

## For NAV-ansatte

Interne henvendelser kan sendes via Slack i kanalen #team-personbruker.