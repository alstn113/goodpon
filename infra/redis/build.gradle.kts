plugins {
    id("java-test-fixtures")
}

dependencies {
    implementation(project(":domain"))

    api("org.springframework.boot:spring-boot-starter-data-redis")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")

    testImplementation(libs.testcontainers)

    testFixturesImplementation(libs.testcontainers)
    testFixturesImplementation("org.springframework.boot:spring-boot-starter-data-redis")
    testFixturesImplementation("org.springframework.boot:spring-boot-starter-test")
}