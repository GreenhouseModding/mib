import dev.greenhouseteam.mib.gradle.Properties
import dev.greenhouseteam.mib.gradle.Versions
import org.apache.tools.ant.filters.LineContains

plugins {
    id("mib.loader")
    id("net.neoforged.moddev")
}

neoForge {
    version = Versions.NEOFORGE
    parchment {
        minecraftVersion = Versions.INTERNAL_MINECRAFT
        mappingsVersion = Versions.PARCHMENT
    }
    addModdingDependenciesTo(sourceSets["test"])

    val at = project(":common").file("src/main/resources/${Properties.MOD_ID}.cfg")
    accessTransformers {
        from(at)
        publish(at)
    }

    runs {
        configureEach {
            systemProperty("forge.logging.markers", "REGISTRIES")
            systemProperty("forge.logging.console.level", "debug")
            systemProperty("neoforge.enabledGameTestNamespaces", Properties.MOD_ID)
        }
        create("client") {
            client()
            gameDirectory.set(file("runs/client"))
            sourceSet = sourceSets["test"]
            jvmArguments.set(setOf("-Dmixin.debug.verbose=true", "-Dmixin.debug.export=true"))
        }
        create("server") {
            server()
            gameDirectory.set(file("runs/server"))
            programArgument("--nogui")
            sourceSet = sourceSets["test"]
            jvmArguments.set(setOf("-Dmixin.debug.verbose=true", "-Dmixin.debug.export=true"))
        }
    }

    mods {
        register(Properties.MOD_ID) {
            sourceSet(sourceSets["main"])
            sourceSet(sourceSets["test"])
        }
    }
}

tasks {
    named<ProcessResources>("processResources").configure {
        filesMatching("*.mixins.json") {
            filter<LineContains>("negate" to true, "contains" to setOf("refmap"))
        }
    }
}