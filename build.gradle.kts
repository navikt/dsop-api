import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    // Apply the Kotlin JVM plugin to add support for Kotlin on the JVM.
    kotlin("jvm").version(Kotlin.version)
    kotlin("plugin.allopen").version(Kotlin.version)
    kotlin("plugin.serialization").version(Kotlin.version)

    id(Shadow.pluginId) version (Shadow.version)
    id(Versions.pluginId) version Versions.version // ./gradlew dependencyUpdates to check for new versions
    application
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "17"
}

repositories {
    mavenCentral()
    maven("https://jitpack.io")
}

dependencies {
    implementation(Auth0.jwt)
    implementation(DittNAV.Common.logging)
    implementation(Ktor.serverNetty)
    implementation(Ktor.serverCallLogging)
    implementation(Ktor.serverCore)
    implementation(Ktor.serialization)
    implementation(Ktor.serializationKotlinx)
    implementation(Ktor.serverCors)
    implementation(Ktor.serverStatusPages)
    implementation(Ktor.serverContentNegotiation)
    implementation(Ktor.serverAuth)
    implementation(Ktor.clientContentNegotiation)
    implementation(Ktor.clientApache)
    implementation(Kotlinx.coroutines)
    implementation(Logback.classic)
    implementation(NAV.tokenValidatorKtor)
    implementation(TmsKtorTokenSupport.tokendingsExchange)
    testImplementation(Assertj.core)
    testImplementation(Ktor.clientMock)
    testImplementation(Ktor.serverTestHost)
    testImplementation(Junit.api)
    testImplementation(Junit.engine)
    testImplementation(Mockk.mockk)
}

application {
    mainClass.set("io.ktor.server.netty.EngineMain")
}

tasks {
    withType<Test> {
        useJUnitPlatform()
        testLogging {
            exceptionFormat = TestExceptionFormat.FULL
            events("passed", "skipped", "failed")
        }
    }
}

apply(plugin = Shadow.pluginId)