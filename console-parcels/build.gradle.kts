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
    const val SPRING_BOOT_STARTER_DATA_JPA = "3.1.4"
    const val POSTGRESQL = "42.5.0"
    const val MAPSTRUCT = "1.6.0"
    const val LOMBOK = "1.18.30"
    const val FLYWAY = "9.16.0"
    const val JUNIT = "5.10.0"
    const val ASSERTJ_CORE = "3.24.2"
    const val MOCKITO = "4.7.0"
    const val JAXB_API = "2.3.1"
    const val JAXB_RUNTIME = "2.3.1"

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
    implementation("net.bytebuddy:byte-buddy:${Version.BYTE_BUDDY}")
    implementation("org.springframework.boot:spring-boot-starter:${Version.SPRING_BOOT}")
    implementation("org.ow2.asm:asm:${Version.ASM}")
    implementation("org.telegram:telegrambots:${Version.TELEGRAM_BOTS}")
    implementation("com.beust:jcommander:${Version.JCOMMANDER}")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:${Version.JACKSON_DATATYPE_JSR310}")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:${Version.SPRING_DOC}")
    implementation("jakarta.persistence:jakarta.persistence-api:${Version.JAKARTA_PERSISTENCE_API}")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa:${Version.SPRING_BOOT_STARTER_DATA_JPA}")
    implementation("org.postgresql:postgresql:${Version.POSTGRESQL}")
    implementation("org.springframework.boot:spring-boot-starter-web:${Version.SPRING_BOOT}")
    implementation("org.mapstruct:mapstruct:${Version.MAPSTRUCT}")
    annotationProcessor("org.projectlombok:lombok-mapstruct-binding:${Version.Lombok.MAP_STRUCT_BINDING}")
    annotationProcessor("org.mapstruct:mapstruct-processor:${Version.MAPSTRUCT}")

    compileOnly("org.projectlombok:lombok:${Version.LOMBOK}")
    annotationProcessor("org.projectlombok:lombok:${Version.LOMBOK}")
    implementation("org.flywaydb:flyway-core:${Version.FLYWAY}")
    testImplementation("org.springframework.boot:spring-boot-starter-test:${Version.SPRING_BOOT}")

    testCompileOnly("org.projectlombok:lombok:${Version.LOMBOK}")
    testAnnotationProcessor("org.projectlombok:lombok:${Version.LOMBOK}")
    testImplementation(platform("org.junit:junit-bom:${Version.JUNIT}"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.assertj:assertj-core:${Version.ASSERTJ_CORE}")
    testImplementation("org.mockito:mockito-inline:${Version.MOCKITO}")
    testImplementation("org.mockito:mockito-core:${Version.MOCKITO}")
    testImplementation("org.mockito:mockito-junit-jupiter:${Version.MOCKITO}")
    testImplementation("org.junit.jupiter:junit-jupiter-api:${Version.JUNIT}")

    implementation("javax.xml.bind:jaxb-api:${Version.JAXB_API}")

    implementation("org.glassfish.jaxb:jaxb-runtime:${Version.JAXB_RUNTIME}")
}

spotless {
    java {
        removeUnusedImports()
    }
}

tasks.test {
    useJUnitPlatform()
}
