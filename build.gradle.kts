import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    kotlin("jvm") version "1.9.23"
    id("io.github.goooler.shadow") version "8.1.7"
    id("io.papermc.paperweight.userdev") version "1.7.1"
}

group = "net.strokkur"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    paperweight.paperDevBundle("1.20.6-R0.1-SNAPSHOT")

    implementation("dev.jorel:commandapi-bukkit-shade-mojang-mapped:9.5.0")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.9.23")
}

tasks.withType<ShadowJar> {
    relocate("dev.jorel.commandapi", "net.strokkur.kotlinplugin.libs.commandapi")
}


tasks.assemble {
    dependsOn(tasks.reobfJar)
}

kotlin {
    jvmToolchain(21)
}