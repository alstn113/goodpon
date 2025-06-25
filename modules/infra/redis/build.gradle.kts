val testcontainersVersion by extra("1.21.1")

dependencies {
    implementation(project(":modules:core"))
    implementation("jakarta.validation:jakarta.validation-api")

    // redis
    implementation("org.springframework.boot:spring-boot-starter-data-redis")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")

    // testcontainers
    testImplementation("org.testcontainers:testcontainers:$testcontainersVersion")
}