object Assertj {
    private const val version = "3.24.0"
    private const val groupId = "org.assertj"

    const val core = "$groupId:assertj-core:$version"

}

object Auth0 {
    private const val version = "4.2.1"
    private const val groupId = "com.auth0"

    const val jwt = "$groupId:java-jwt:$version"
}

object Kotlin {
    const val version = "1.8.0"
    private const val groupId = "org.jetbrains.kotlin"

    const val junit5 = "$groupId:kotlin-test-junit5:$version"
}

object Kotlinx {
    private const val groupId = "org.jetbrains.kotlinx"

    const val coroutines = "$groupId:kotlinx-coroutines-core:1.6.4"
}

object Ktor {
    private const val version = "2.2.2"
    private const val groupId = "io.ktor"

    const val metricsMicrometer = "$groupId:ktor-server-metrics-micrometer:$version"
    const val serverNetty = "$groupId:ktor-server-netty:$version"
    const val serverCallLogging = "$groupId:ktor-server-call-logging:$version"
    const val serverCore = "$groupId:ktor-server-core:$version"
    const val serialization = "$groupId:ktor-serialization:$version"
    const val serializationKotlinx = "$groupId:ktor-serialization-kotlinx-json:$version"
    const val serverCors = "$groupId:ktor-server-cors:$version"
    const val serverStatusPages = "$groupId:ktor-server-status-pages:$version"
    const val serverContentNegotiation = "$groupId:ktor-server-content-negotiation:$version"
    const val serverAuth = "$groupId:ktor-server-auth:$version"
    const val serverTestHost = "$groupId:ktor-server-test-host:$version"
    const val clientContentNegotiation = "$groupId:ktor-client-content-negotiation:$version"
    const val clientApache = "$groupId:ktor-client-apache:$version"
    const val clientMock = "$groupId:ktor-client-mock:$version"
}

object Logback {
    private const val version = "1.4.5"
    const val classic = "ch.qos.logback:logback-classic:$version"
}

object Logstash {
    private const val version = "7.2"
    const val logbackEncoder = "net.logstash.logback:logstash-logback-encoder:$version"
}

object Micrometer {
    private const val version = "1.10.2"
    const val registryPrometheus = "io.micrometer:micrometer-registry-prometheus:$version"
}

object Mockk {
    private const val version = "1.13.3"
    const val mockk = "io.mockk:mockk:$version"
}

object NAV {
    const val tokenValidatorKtor = "no.nav.security:token-validation-ktor-v2:3.0.2"
}

object Shadow {
    const val version = "7.1.2"
    const val pluginId = "com.github.johnrengelman.shadow"
}

object TmsKtorTokenSupport {
    private const val version = "2.0.0"
    private const val groupId = "com.github.navikt.tms-ktor-token-support"

    const val tokendingsExchange = "$groupId:token-support-tokendings-exchange:$version"
}

object Versions {
    const val version = "0.44.0"
    const val pluginId = "com.github.ben-manes.versions"
}