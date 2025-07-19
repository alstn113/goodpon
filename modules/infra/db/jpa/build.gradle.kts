plugins {
    alias(libs.plugins.kotlin.jpa)
    id("java-test-fixtures")
    id("com.google.devtools.ksp") version "1.9.25-1.0.20" // for querydsl
}

val querydslVersion by extra("7.0")

dependencies {
    implementation(project(":modules:domain"))
    implementation(project(":modules:application:partner-application"))
    implementation(project(":modules:application:dashboard-application"))

    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    runtimeOnly("com.h2database:h2")
    runtimeOnly("com.mysql:mysql-connector-j")

    implementation("io.github.openfeign.querydsl:querydsl-jpa:${querydslVersion}")
    annotationProcessor("io.github.openfeign.querydsl:querydsl-apt:${querydslVersion}:jpa")
    ksp("io.github.openfeign.querydsl:querydsl-ksp-codegen:${querydslVersion}")

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