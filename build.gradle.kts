import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

group = "me.jakejmattson"
version = Versions.BOT

plugins {
    kotlin("jvm") version "1.3.72"
}

repositories {
    mavenCentral()
    mavenLocal()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("me.jakejmattson:KUtils:${Versions.KUTILS}")
}

tasks {
    withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "1.8"
    }

    copy {
        from(file("src/main/resources/templates/readme-template.md"))
        into(file("."))
        rename{ "README.md" }
        expand(
            "kotlin" to Versions.KOTLIN,
            "kutils" to Versions.KUTILS
        )
    }
}

object Versions {
    const val BOT = "2.0.1"
    const val KOTLIN = "1.3.72"
    const val KUTILS = "0.17.1"
}