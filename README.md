# Mib

Musical Instrument Library (Mib), is a mod that adds musical instruments meant to be interfaced by other mods.

This mod was made for ModFest Carnival, and was going to have a sister mod, [Orchestrate](https://github.com/GreenhouseTeam/orchestrate), but that mod has been delayed until after the fest.

## For Developers
To depend on Mib, you should use the Greenhouse Maven.
```groovy
repositories {
    maven {
        name = "Greenhouse Maven"
        url = 'https://maven.greenhouseteam.dev/releases/'
    }
}
dependencies {
    // Depend on the Common project, for VanillaGradle/ModDevGradle projects.
    compileOnly("dev.greenhouseteam:mib-common:${mib_version}")

    // Depend on the Fabric project, for Fabric or Quilt.
    modImplementation("dev.greenhouseteam:mib-fabric:${mib_version}")

    // Depend on the NeoForge project, for NeoForge.
    implementation("dev.greenhouseteam:mib-neoforge:${mib_version}")
}
```