plugins {
    id("java")
    id("org.springframework.boot") version "3.3.3"
    id ("com.diffplug.spotless")      version "6.22.0"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.shell:spring-shell-starter:3.1.4")
    implementation("org.springframework.boot:spring-boot-starter-web:3.3.3")
    implementation("org.springframework.boot:spring-boot-starter:3.3.3")
    annotationProcessor("org.projectlombok:lombok-mapstruct-binding:0.2.0")
    compileOnly("org.projectlombok:lombok:1.18.30")

    annotationProcessor("org.projectlombok:lombok-mapstruct-binding:0.2.0")
    annotationProcessor ("org.projectlombok:lombok:1.18.30")
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