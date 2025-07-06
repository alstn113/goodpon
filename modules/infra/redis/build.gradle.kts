val testcontainersVersion by extra("1.21.1")

dependencies {
    implementation(project(":modules:domain"))
    implementation(project(":modules:application:dashboard-application"))

    implementation("org.springframework.boot:spring-boot-starter-data-redis")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")

    testImplementation("org.testcontainers:testcontainers:$testcontainersVersion")
}