object Version {
    const val SPRING_BOOT = "3.1.4" // Use the same version for all Spring Boot dependencies
    const val JAKARTA_PERSISTENCE_API = "3.1.0" // Updated to 3.1.0 for compatibility
    const val LOMBOK = "1.18.30"
    const val LOMBOK_MAPSTRUCT_BINDING = "0.2.0"
    const val JUNIT = "5.10.0" // Use a single version for JUnit
    const val MAPSTRUCT = "1.6.0"
    const val POSTGRESQL = "42.5.0"
    const val SPRING_KAFKA = "3.0.10" // Added for Kafka
    const val SPRING_DOC = "2.2.0"
}

plugins {
    id("java")
    id("org.springframework.boot") version "3.1.4"
    id("io.spring.dependency-management") version "1.1.3" // Add dependency management plugin
    id("com.diffplug.spotless") version "6.19.0"
}

group = "com.hofftech.omni"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    // Spring Boot Starters
    implementation("org.springframework.boot:spring-boot-starter:${Version.SPRING_BOOT}")
    implementation("org.springframework.boot:spring-boot-starter-web:${Version.SPRING_BOOT}")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa:${Version.SPRING_BOOT}")
    implementation("org.springframework.boot:spring-boot-starter-jdbc:${Version.SPRING_BOOT}") // Required for JdbcClient

    // Database
    implementation("jakarta.persistence:jakarta.persistence-api:${Version.JAKARTA_PERSISTENCE_API}")
    implementation("org.postgresql:postgresql:${Version.POSTGRESQL}")

    //Cache
    implementation("org.springframework.boot:spring-boot-starter-cache")
    implementation("com.github.ben-manes.caffeine:caffeine")

    // Kafka
    implementation("org.springframework.kafka:spring-kafka:${Version.SPRING_KAFKA}")

    // MapStruct
    implementation("org.mapstruct:mapstruct:${Version.MAPSTRUCT}")
    annotationProcessor("org.mapstruct:mapstruct-processor:${Version.MAPSTRUCT}")

    // Lombok
    compileOnly("org.projectlombok:lombok:${Version.LOMBOK}")
    annotationProcessor("org.projectlombok:lombok:${Version.LOMBOK}")
    annotationProcessor("org.projectlombok:lombok-mapstruct-binding:${Version.LOMBOK_MAPSTRUCT_BINDING}")

    // Testing
    testImplementation("org.springframework.boot:spring-boot-starter-test:${Version.SPRING_BOOT}")
    testImplementation(platform("org.junit:junit-bom:${Version.JUNIT}"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    // SpringDoc OpenAPI
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:${Version.SPRING_DOC}")
}

spotless {
    java {
        removeUnusedImports()
    }
}

tasks.test {
    useJUnitPlatform()
}