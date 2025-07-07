plugins {
    id("java-test-fixtures")
}

dependencies {
    implementation(project(":modules:domain"))
    implementation(project(":modules:application:dashboard-application"))

    implementation("org.springframework.boot:spring-boot-starter-data-redis")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")

    testImplementation(libs.testcontainers)

    testFixturesImplementation(libs.testcontainers)
    testFixturesImplementation("org.springframework.boot:spring-boot-starter-data-redis")
    testFixturesImplementation("org.springframework.boot:spring-boot-starter-test")
}