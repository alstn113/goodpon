import org.apache.tools.ant.util.ScriptManager.auto

plugins {
    id("java-test-fixtures")
}

dependencies {
    implementation(project(":modules:domain"))
    implementation(project(":modules:application:partner-application"))
    implementation("org.springframework.kafka:spring-kafka")
    implementation("org.springframework.boot:spring-boot-autoconfigure")

    testImplementation(libs.testcontainers)
    testImplementation(libs.testcontainers.kafka)

    testFixturesImplementation(libs.testcontainers)
    testFixturesImplementation(libs.testcontainers.kafka)
    testFixturesImplementation("org.springframework.boot:spring-boot-starter-test")
    testFixturesImplementation("org.springframework.kafka:spring-kafka-test")
}