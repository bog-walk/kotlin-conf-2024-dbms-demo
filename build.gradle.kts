plugins {
    kotlin("multiplatform") apply false
    kotlin("plugin.serialization") apply false
    id("io.ktor.plugin") apply false
    id("org.jetbrains.compose") apply false
    id("org.jetbrains.kotlin.plugin.compose") apply false
}

group = "dev.bogwalk"
version = "1.0-SNAPSHOT"

allprojects {
    repositories {
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        google()
        mavenCentral()
    }
}
