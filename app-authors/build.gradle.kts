plugins {
    id("java")
    id("org.springframework.boot") version "3.5.3"
    id("io.spring.dependency-management") version "1.1.7"
}

group = "com.programacion.distribuida"
version = "unspecified"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

dependencyManagement {
    imports {
        mavenBom ("org.springframework.cloud:spring-cloud-dependencies:2025.0.0")
    }
}


dependencies {
    // JPA: ORM con Hibernate/JPA
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")

    // Web REST y JSON (Tomcat + Jackson)
    implementation("org.springframework.boot:spring-boot-starter-web")

    // Migraciones automáticas de BD (Flyway)
    implementation("org.flywaydb:flyway-core")

    // Driver Flyway para PostgreSQL
    implementation("org.flywaydb:flyway-database-postgresql")

    // Descubrimiento de servicios con Consul
    implementation("org.springframework.cloud:spring-cloud-starter-consul-discovery")

    // Actuator: métricas y health checks
    implementation("org.springframework.boot:spring-boot-starter-actuator")

    // Prometheus: métricas
    implementation("io.micrometer:micrometer-registry-prometheus")




    // Lombok: anotaciones (solo compilación)
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")

    // Driver PostgreSQL (runtime)
    runtimeOnly("org.postgresql:postgresql")


}

tasks.test {
    useJUnitPlatform()
}