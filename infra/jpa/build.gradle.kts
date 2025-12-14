plugins {
    alias(libs.plugins.kotlin.jpa)
    alias(libs.plugins.ksp)
    id("java-test-fixtures")
}

dependencies {
    implementation(project(":domain"))

    api("org.springframework.boot:spring-boot-starter-data-jpa")
    runtimeOnly("com.h2database:h2")
    runtimeOnly("com.mysql:mysql-connector-j")

    implementation(libs.querydsl.jpa)
    annotationProcessor(libs.querydsl.apt)
    ksp(libs.querydsl.ksp.codegen)

    testImplementation(libs.testcontainers)
    testImplementation(libs.testcontainers.mysql)

    testFixturesImplementation(libs.testcontainers)
    testFixturesImplementation(libs.testcontainers.mysql)
    testFixturesImplementation("org.springframework.boot:spring-boot-starter-data-jpa")
    testFixturesImplementation("org.springframework.boot:spring-boot-starter-test")
}

allOpen {
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.persistence.MappedSuperclass")
    annotation("jakarta.persistence.Embeddable")
}