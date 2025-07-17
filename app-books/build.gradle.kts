plugins {
    java
    id("org.springframework.boot") version "3.5.3"
    id("io.spring.dependency-management") version "1.1.7"
}

group = "com.programacion.distribuida"
version = "unspecified"

java.toolchain.languageVersion.set(JavaLanguageVersion.of(21))

repositories {
    mavenCentral()
}

configurations.compileOnly {
    extendsFrom(configurations.annotationProcessor.get())
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

    // Mapeo de objetos: ModelMapper
    implementation("org.modelmapper:modelmapper:3.2.3")

    // Descubrimiento de servicios con Consul
    implementation("org.springframework.cloud:spring-cloud-starter-consul-discovery")

    // Cliente declarativo OpenFeign
    implementation("org.springframework.cloud:spring-cloud-starter-openfeign:4.3.0")

    // Actuator: métricas y health checks
    implementation("org.springframework.boot:spring-boot-starter-actuator")

    // Prometheus: métricas
    implementation("io.micrometer:micrometer-registry-prometheus")

    // Lombok: anotaciones (solo compilación)
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")

    // Driver PostgreSQL (runtime)
    runtimeOnly("org.postgresql:postgresql")

    // Pruebas
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}


tasks.test {
    useJUnitPlatform()
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:2025.0.0")
    }
}

