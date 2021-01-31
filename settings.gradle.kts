/*
 * Copyright 2021 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

pluginManagement {
    repositories {
        gradlePluginPortal()
        jcenter()
        google()

        maven { url = uri("https://plugins.gradle.org/m2/") }
        maven { url = uri("https://dl.bintray.com/kotlin/kotlin") }
        maven { url = uri("https://kotlin.bintray.com/kotlinx") }
    }
}

dependencyResolutionManagement {
    repositories {
        mavenCentral()
        jcenter()
    }
}

rootProject.name = "futures-kotlin-api"