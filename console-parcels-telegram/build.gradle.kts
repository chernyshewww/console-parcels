object Version {
    const val SPRING_BOOT = "3.3.3"
    const val TELEGRAM_BOTS = "6.5.0"
    const val LOMBOK = "1.18.30"
    const val LOMBOK_MAPSTRUCT_BINDING = "0.2.0"
    const val JUNIT_BOM = "5.10.0"
    const val JUNIT_JUPITER = "5.10.0"
}

plugins {
    id("java")
    id("org.springframework.boot") version "3.1.4"
    id("com.diffplug.spotless") version "6.19.0"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter:${Version.SPRING_BOOT}")
    implementation("org.telegram:telegrambots:${Version.TELEGRAM_BOTS}")
    implementation("org.springframework.boot:spring-boot-starter-web:${Version.SPRING_BOOT}")
    annotationProcessor("org.projectlombok:lombok-mapstruct-binding:${Version.LOMBOK_MAPSTRUCT_BINDING}")
    compileOnly("org.projectlombok:lombok:${Version.LOMBOK}")

    annotationProcessor("org.projectlombok:lombok-mapstruct-binding:${Version.LOMBOK_MAPSTRUCT_BINDING}")
    annotationProcessor("org.projectlombok:lombok:${Version.LOMBOK}")
    testImplementation(platform("org.junit:junit-bom:${Version.JUNIT_BOM}"))
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
