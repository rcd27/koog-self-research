plugins {
    kotlin("jvm")
    id("org.jetbrains.kotlin.plugin.serialization") version "1.9.22"
    application
}

application {
    mainClass.set("MainKt")
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.2")

    /** Koog */
    implementation("ai.koog:koog-agents:0.5.2")

    /** OpenTelemetry */
    implementation(platform("io.opentelemetry:opentelemetry-bom:1.53.0"))
    implementation("io.opentelemetry:opentelemetry-sdk")
    implementation("io.opentelemetry:opentelemetry-exporter-otlp")
    implementation("io.opentelemetry:opentelemetry-exporter-logging")

    /** Logging */
    implementation("io.github.oshai:kotlin-logging-jvm:7.0.0")
    implementation("ch.qos.logback:logback-classic:1.5.13")

    /** Network */
    implementation("io.ktor:ktor-client-cio:3.3.1")
    implementation("io.ktor:ktor-client-core:3.3.1")
    implementation("io.ktor:ktor-client-content-negotiation:3.3.1")
    implementation("io.ktor:ktor-serialization-kotlinx-json:3.3.1")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0")

    /** Environment Variables */
    implementation("io.github.cdimascio:dotenv-kotlin:6.4.1")

    /** CLI */
    implementation("org.jetbrains.kotlinx:kotlinx-cli:0.3.6")

    /** Tests */
    testImplementation(kotlin("test"))
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")
    testImplementation("ai.koog:agents-test:0.5.2")
    testImplementation("io.mockk:mockk:1.13.8")
}

kotlin {
    jvmToolchain(17)

    // https://kotlinlang.org/docs/context-parameters.html#how-to-enable-context-parameters
    compilerOptions {
        freeCompilerArgs.add("-Xcontext-parameters")
    }
}

tasks.test {
    useJUnitPlatform()
}

application {
    mainClass.set("com.github.rcd27.koogselfresearch.MainKt")
}
