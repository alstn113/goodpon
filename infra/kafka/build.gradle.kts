plugins {
    id("java-test-fixtures")
}

dependencies {
    implementation(project(":domain"))
    api("org.springframework.kafka:spring-kafka")
    api("org.springframework.boot:spring-boot-starter-json") // for ObjectMapper

    testImplementation(libs.testcontainers)
    testImplementation(libs.testcontainers.kafka)

    testFixturesImplementation(libs.testcontainers)
    testFixturesImplementation(libs.testcontainers.kafka)
    testFixturesImplementation("org.springframework.boot:spring-boot-starter-test")
    testFixturesImplementation("org.springframework.kafka:spring-kafka-test")
}