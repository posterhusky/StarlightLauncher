plugins {
    kotlin("jvm") version "2.0.0"
    id("com.github.johnrengelman.shadow") version "7.0.0"
    kotlin("plugin.serialization") version "2.0.0"
}

group = "net.vanolex"
version = "1.2-rc1"

var ktor_version = "3.1.0"
var coroutines_version = "1.9.0-RC.2"

repositories {
    mavenCentral()
    maven(url = "https://jitpack.io")
}

dependencies {
    implementation("io.ktor:ktor-client-okhttp-jvm:3.1.0")
    testImplementation(kotlin("test"))
    implementation("io.ktor:ktor-client-core:$ktor_version")
    implementation("io.ktor:ktor-client-okhttp:$ktor_version")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutines_version")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-swing:$coroutines_version")
    implementation("com.google.code.gson:gson:2.12.1")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:1.5.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.0")
    implementation("ch.qos.logback:logback-classic:1.5.17")
    implementation("com.github.steos:jnafilechooser:1.1.2")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}

tasks.withType<JavaExec> {
    jvmArgs = listOf("-Dsun.java2d.opengl=true")
}

tasks {
    jar {
        manifest {
            attributes["Main-Class"] = "net.vanolex.MainKt"
        }
    }

    build {
        dependsOn(shadowJar)
    }
    shadowJar {
        mergeServiceFiles()
        archiveClassifier = ""
    }
}

sourceSets {
    main {
        kotlin {
            srcDir("$buildDir/generated/source/version")
        }
    }
}

// 2) Define a task that writes a small Kotlin object containing the version
val generateVersionFile = tasks.register("generateVersionFile") {
    outputs.dir("$buildDir/generated/source/version")
    doLast {
        val outputDir = file("$buildDir/generated/source/version")
        outputDir.mkdirs()

        val versionKt = File(outputDir, "Version.kt")
        versionKt.writeText("""
            // Auto‐generated by Gradle; do not edit by hand.
            package net.vanolex

            object Version {
                const val CURRENT = "${project.version}"
            }
        """.trimIndent())
    }
}

// 3) Make sure the Kotlin compiler runs **after** we generate Version.kt
tasks.named("compileKotlin") {
    dependsOn(generateVersionFile)
}
