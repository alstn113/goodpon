plugins {
    id("java-test-fixtures")
}

dependencies {
    implementation(project(":modules:domain"))
    implementation(project(":modules:application:partner-application"))
    implementation("org.springframework.kafka:spring-kafka")

    testImplementation(libs.testcontainers)
    testImplementation(libs.testcontainers.kafka)

    testFixturesImplementation(libs.testcontainers)
    testFixturesImplementation(libs.testcontainers.kafka)
    testFixturesImplementation("org.springframework.boot:spring-boot-starter-test")
    testFixturesImplementation("org.springframework.kafka:spring-kafka-test")
}