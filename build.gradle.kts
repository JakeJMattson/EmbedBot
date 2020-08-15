import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

group = "me.jakejmattson"
version = Versions.BOT

plugins {
    kotlin("jvm") version "1.4.0"
    id("com.github.johnrengelman.shadow") version "6.0.0"
}

repositories {
    mavenCentral()
    jcenter()
}

dependencies {
    implementation("me.jakejmattson:DiscordKt:${Versions.DISCORDKT}")
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
            "kotlin" to kotlin.coreLibrariesVersion,
            "discordkt" to Versions.DISCORDKT
        )
    }

    shadowJar {
        archiveFileName.set("EmbedBot.jar")
        manifest {
            attributes(
                "Main-Class" to "me.jakejmattson.embedbot.MainAppKt"
            )
        }
    }
}

object Versions {
    const val BOT = "2.1.0"
    const val DISCORDKT = "0.19.1"
}