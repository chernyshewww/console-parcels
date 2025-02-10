object Version {
    const val SPRING_BOOT = "3.1.4"
    const val SLF4J = "2.0.0"
    const val LOGBACK = "1.4.5"
    const val JACKSON = "2.15.2"
    const val BYTE_BUDDY = "1.14.7"
    const val ASM = "9.5"
    const val TELEGRAM_BOTS = "6.5.0"
    const val JCOMMANDER = "1.78"
    const val JACKSON_DATATYPE_JSR310 = "2.13.0"
    const val SPRING_DOC = "2.2.0"
    const val JAKARTA_PERSISTENCE_API = "3.0.0"
    const val POSTGRESQL = "42.5.0"
    const val MAPSTRUCT = "1.6.0"
    const val LOMBOK = "1.18.30"
    const val FLYWAY = "9.16.0"
    const val JUNIT = "5.10.0"
    const val ASSERTJ_CORE = "3.24.2"
    const val MOCKITO = "4.7.0"
    const val JAXB_API = "2.3.1"
    const val JAXB_RUNTIME = "2.3.1"
    const val GSON = "2.11.0"

    object Spring {
        const val CLOUD = "2023.0.3"
        const val OPEN_API = "2.6.0"
    }

    object Lombok {
        const val MAP_STRUCT_BINDING = "0.2.0"
    }
}

plugins {
    id("java")
    id("com.diffplug.spotless") version "6.19.0"
    id("org.springframework.boot") version "3.1.4"
}

group = "com.deliverysystem"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.slf4j:slf4j-api:${Version.SLF4J}")
    implementation("ch.qos.logback:logback-classic:${Version.LOGBACK}")

    implementation("com.fasterxml.jackson.core:jackson-databind:${Version.JACKSON}")
    implementation("com.fasterxml.jackson.core:jackson-annotations:${Version.JACKSON}")
    implementation("com.fasterxml.jackson.core:jackson-core:${Version.JACKSON}")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:${Version.JACKSON_DATATYPE_JSR310}")

    implementation("org.springframework.boot:spring-boot-starter:${Version.SPRING_BOOT}")
    implementation("org.springframework.boot:spring-boot-starter-web:${Version.SPRING_BOOT}")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa:${Version.SPRING_BOOT}")
    implementation("org.springframework.kafka:spring-kafka:3.0.10")

    implementation("org.postgresql:postgresql:${Version.POSTGRESQL}")
    implementation("org.flywaydb:flyway-core:${Version.FLYWAY}")
    implementation("jakarta.persistence:jakarta.persistence-api:${Version.JAKARTA_PERSISTENCE_API}")

    implementation("org.mapstruct:mapstruct:${Version.MAPSTRUCT}")
    annotationProcessor("org.mapstruct:mapstruct-processor:${Version.MAPSTRUCT}")
    annotationProcessor("org.projectlombok:lombok-mapstruct-binding:${Version.Lombok.MAP_STRUCT_BINDING}")

    compileOnly("org.projectlombok:lombok:${Version.LOMBOK}")
    annotationProcessor("org.projectlombok:lombok:${Version.LOMBOK}")
    testCompileOnly("org.projectlombok:lombok:${Version.LOMBOK}")
    testAnnotationProcessor("org.projectlombok:lombok:${Version.LOMBOK}")

    testImplementation("org.springframework.boot:spring-boot-starter-test:3.4.1")
    testImplementation(platform("org.junit:junit-bom:${Version.JUNIT}"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.assertj:assertj-core:${Version.ASSERTJ_CORE}")
    testImplementation("org.mockito:mockito-inline:${Version.MOCKITO}")
    testImplementation("org.mockito:mockito-core:${Version.MOCKITO}")
    testImplementation("org.mockito:mockito-junit-jupiter:${Version.MOCKITO}")
    testImplementation("org.junit.jupiter:junit-jupiter-api:${Version.JUNIT}")

    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testRuntimeOnly ("org.junit.platform:junit-platform-launcher")
    testImplementation ("org.testcontainers:postgresql:1.17.6")
    testImplementation ("org.testcontainers:junit-jupiter:1.17.6")
    testImplementation ("org.springframework.boot:spring-boot-testcontainers:3.4.1")
    implementation("javax.xml.bind:jaxb-api:${Version.JAXB_API}")
    implementation("org.glassfish.jaxb:jaxb-runtime:${Version.JAXB_RUNTIME}")

    implementation("com.beust:jcommander:${Version.JCOMMANDER}")

    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:${Version.SPRING_DOC}")

    implementation("com.google.code.gson:gson:${Version.GSON}")
}

spotless {
    java {
        removeUnusedImports()
    }
}

tasks.test {
    useJUnitPlatform()
}