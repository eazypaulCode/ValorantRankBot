import java.util.regex.Pattern.compile

plugins {
    java
    application
}

group = "de.eazypaulcode"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

application {
    mainClass.set("de.eazypaulcode.valorantrankbot.Main")
}

dependencies {
    implementation("net.dv8tion:JDA:5.0.0-alpha.9")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.2")
    implementation("com.konghq:unirest-java:3.11.09")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}

tasks.withType<Jar> {
    val classpath = configurations.runtimeClasspath

    inputs.files(classpath).withNormalizer(ClasspathNormalizer::class.java)

    manifest {
        attributes["Main-Class"] = "de.eazypaulcode.valorantrankbot.Main"

        attributes(
            "Class-Path" to classpath.map { cp -> cp.joinToString(" ") { "./lib/" + it.name } }
        )
    }
    archiveBaseName.set("ValorantRankBot")
    archiveVersion.set("")
}