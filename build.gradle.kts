plugins {
    id("java")
    id("org.springframework.boot") version("3.2.0")
    id("io.spring.dependency-management") version("1.1.0")
}

group = "ru.homework"
version = "1.0-SNAPSHOT"

val postgresql = "42.7.5"
val flyway = "9.16.3"
val mapstruct = "1.6.3"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-mail")
    implementation("org.springframework.kafka:spring-kafka")
    implementation("org.postgresql:postgresql:$postgresql")
    implementation("org.flywaydb:flyway-core:${flyway}")
    implementation("org.mapstruct:mapstruct:${mapstruct}")
    annotationProcessor("org.mapstruct:mapstruct-processor:${mapstruct}")
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}