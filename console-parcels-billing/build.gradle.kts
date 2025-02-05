object Version {
    const val JAKARTA_PERSISTENCE_API = "3.0.0"
    const val SPRING_BOOT = "3.3.3"
    const val LOMBOK = "1.18.30"
    const val LOMBOK_MAPSTRUCT_BINDING = "0.2.0"
    const val JUNIT_BOM = "5.10.0"
    const val JUNIT_JUPITER = "5.10.0"
    const val MAPSTRUCT = "1.6.0"
    const val SPRING_BOOT_STARTER_DATA_JPA = "3.1.4"
    const val POSTGRESQL = "42.5.0"
}

plugins {
    id("java")
    id("org.springframework.boot") version "3.1.4"
    id("com.diffplug.spotless") version "6.19.0"
}

group = "com.hofftech.omni"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter:${Version.SPRING_BOOT}")
    implementation("org.springframework.boot:spring-boot-starter-web:${Version.SPRING_BOOT}")
    annotationProcessor("org.projectlombok:lombok-mapstruct-binding:${Version.LOMBOK_MAPSTRUCT_BINDING}")
    implementation("org.mapstruct:mapstruct:${Version.MAPSTRUCT}")
    implementation("jakarta.persistence:jakarta.persistence-api:${Version.JAKARTA_PERSISTENCE_API}")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa:${Version.SPRING_BOOT_STARTER_DATA_JPA}")
    implementation("org.postgresql:postgresql:${Version.POSTGRESQL}")

    compileOnly("org.projectlombok:lombok:${Version.LOMBOK}")

    annotationProcessor("org.projectlombok:lombok-mapstruct-binding:${Version.LOMBOK_MAPSTRUCT_BINDING}")
    annotationProcessor("org.projectlombok:lombok:${Version.LOMBOK}")
    testImplementation(platform("org.junit:junit-bom:${Version.JUNIT_BOM}"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
}

spotless {
    java {
        removeUnusedImports()
    }
}

tasks.test {
    useJUnitPlatform()
}