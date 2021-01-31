/*
 * Copyright 2021 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

plugins {
    val kotlinVersion = "1.4.21"

    id("org.jetbrains.kotlin.jvm") version (kotlinVersion)
    id("org.jetbrains.kotlin.plugin.serialization") version (kotlinVersion)
}

group = "dev.icerock.binance"
version = "0.1.0"

dependencies {
    val coroutinesVersion = "1.4.2"
    val ktorClientVersion = "1.5.1"
    val serializationVersion = "1.0.1"
    val junitVersion = "4.13"

    api("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
    api("io.ktor:ktor-client:$ktorClientVersion")
    api("io.ktor:ktor-client-logging:$ktorClientVersion")
    api("org.jetbrains.kotlinx:kotlinx-serialization-json:$serializationVersion")

    implementation("io.ktor:ktor-client-okhttp:$ktorClientVersion")
    implementation("io.ktor:ktor-client-websockets:$ktorClientVersion")
    implementation("io.ktor:ktor-client-serialization:$ktorClientVersion")

    testImplementation("junit:junit:$junitVersion")
}
