# DSOP-Api

![Deploy-to-prod](https://github.com/navikt/dsop-api/workflows/Deploy-to-prod/badge.svg) | ![Deploy-to-dev](https://github.com/navikt/dsop-api/workflows/Deploy-to-dev/badge.svg)

Api bygget på Ktor for å koble seg opp mot DSOP / sporingslogg 

## Deployering

Dev:
```
git tag -a vX.X.X-dev
```

Prod:
```
git tag -a vX.X.X-prod
```

Push den nye versjonen til GitHub og merge til master.
```
git push && git push --tags
```

## Lokal Kjøring

For å kjøre opp løsningen lokalt, kjør main-funksjonen i [DevApplication](https://github.com/navikt/dsop-api/blob/master/src/test/kotlin/no/nav/sbl/dsop/api/DevApplication.kt).
Denne vil by-passe autentisering, mens endepunkter mot dsop, ereg og kodeverk vil mockes ut. Appen vil da svare på endepunktet http://localhost:8080/person/dsop-api/get. 

## Logging

Feil ved API-kall blir logget via frontendlogger og vises i Kibana<br>
[https://logs.adeo.no](https://logs.adeo.no/app/kibana#/discover/ad01c200-4af4-11e9-a5a6-7fddb220bd0c)

## Henvendelser

Spørsmål knyttet til koden eller prosjektet kan rettes mot https://github.com/orgs/navikt/teams/personbruker

## For NAV-ansatte

Interne henvendelser kan sendes via Slack i kanalen #team-personbruker.
