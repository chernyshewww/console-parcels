plugins {
    id("java")
    id ("com.diffplug.spotless")      version "6.19.0"
}

group = "com.deliverysystem"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.slf4j:slf4j-api:2.0.0")
    implementation("ch.qos.logback:logback-classic:1.4.5")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

spotless {
    java {
        removeUnusedImports()
    }
}
tasks.test {
    useJUnitPlatform()
}