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
    implementation("com.fasterxml.jackson.core:jackson-databind:2.15.2")
    implementation("com.fasterxml.jackson.core:jackson-annotations:2.15.2")
    implementation("com.fasterxml.jackson.core:jackson-core:2.15.2")
    implementation("net.bytebuddy:byte-buddy:1.14.7")
    implementation("org.springframework.boot:spring-boot-starter:3.1.4")
    implementation("org.ow2.asm:asm:9.5")
    compileOnly("org.projectlombok:lombok:1.18.30")
    annotationProcessor("org.projectlombok:lombok:1.18.30")

    testCompileOnly("org.projectlombok:lombok:1.18.30")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.30")
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.assertj:assertj-core:3.24.2")
    testImplementation("org.mockito:mockito-inline:4.7.0")
}

spotless {
    java {
        removeUnusedImports()
    }
}
tasks.test {
    useJUnitPlatform()
}