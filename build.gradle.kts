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
val loggerStarter = "1.0"
val testContainers = "1.20.0"
val mockito = "5.12.0"

repositories {
    mavenLocal()
    mavenCentral()
}

dependencies {
    implementation("ru.homework:spring-starter-logger:$loggerStarter")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-mail")
    implementation("org.springframework.kafka:spring-kafka")
    implementation("org.postgresql:postgresql:$postgresql")
    implementation("org.flywaydb:flyway-core:${flyway}")
    implementation("org.mapstruct:mapstruct:${mapstruct}")
    annotationProcessor("org.mapstruct:mapstruct-processor:${mapstruct}")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.testcontainers:postgresql:$testContainers")
    testImplementation("org.testcontainers:kafka:$testContainers")
    testImplementation("org.testcontainers:junit-jupiter:$testContainers")
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.mockito:mockito-core:$mockito")
    testImplementation("org.mockito:mockito-junit-jupiter:$mockito")
}

tasks.test {
    useJUnitPlatform()
}