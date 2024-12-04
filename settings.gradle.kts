@Suppress("UnstableApiUsage") // dependencyResolutionManagement is still 'incubating' in Gradle 8.11.1
dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}

rootProject.name = "advent-of-code-2024-in-kotlin"
