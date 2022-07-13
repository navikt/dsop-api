object Assertj {
    private const val version = "3.23.1"
    private const val groupId = "org.assertj"

    const val core = "$groupId:assertj-core:$version"

}

object DittNAV {

    object Common {
        private const val version = "2022.04.19-11.11-1043a85c4f6f"

        private const val groupId = "com.github.navikt.dittnav-common-lib"
        const val logging = "$groupId:dittnav-common-logging:$version"
    }
}

object Junit {
    private const val version = "5.8.2"

    private const val groupId = "org.junit.jupiter"
    const val api = "$groupId:junit-jupiter-api:$version"
    const val engine = "$groupId:junit-jupiter-engine:$version"
}

object Kotlin {
    const val version = "1.6.21"
}

object Kotlinx {
    private const val groupId = "org.jetbrains.kotlinx"

    const val coroutines = "$groupId:kotlinx-coroutines-core:1.6.3"
}

object Ktor {
    private const val version = "2.0.2"
    private const val groupId = "io.ktor"

    const val serverNetty = "$groupId:ktor-server-netty:$version"
    const val serverCallLogging = "$groupId:ktor-server-call-logging:$version"
    const val serverCore = "$groupId:ktor-server-core:$version"
    const val serialization = "$groupId:ktor-serialization:$version"
    const val serializationGson = "$groupId:ktor-serialization-gson:$version"
    const val serverCors = "$groupId:ktor-server-cors:$version"
    const val serverStatusPages = "$groupId:ktor-server-status-pages:$version"
    const val serverContentNegotiation = "$groupId:ktor-server-content-negotiation:$version"
    const val serverAuth = "$groupId:ktor-server-auth:$version"
    const val clientContentNegotiation = "$groupId:ktor-client-content-negotiation:$version"
    const val clientApache = "$groupId:ktor-client-apache:$version"
    const val clientMock = "$groupId:ktor-client-mock:$version"
}

object Logback {
    private const val version = "1.2.11"
    const val classic = "ch.qos.logback:logback-classic:$version"
}

object Mockk {
    private const val version = "1.12.4"
    const val mockk = "io.mockk:mockk:$version"
}

object NAV {
    const val tokenValidatorKtor = "no.nav.security:token-validation-ktor-v2:2.1.1"
}

object Shadow {
    const val version = "7.1.2"
    const val pluginId = "com.github.johnrengelman.shadow"
}

object TmsKtorTokenSupport {
    private const val version = "2022.05.19-09.32-5076b2435b0a"
    private const val groupId = "com.github.navikt.tms-ktor-token-support"

    const val tokendingsExchange = "$groupId:token-support-tokendings-exchange:$version"
}

object Versions {
    const val version = "0.42.0"
    const val pluginId = "com.github.ben-manes.versions"
}