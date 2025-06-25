val testcontainersVersion by extra("1.21.1")

dependencies {
    implementation(project(":modules:core"))
    implementation("jakarta.validation:jakarta.validation-api")

    // security
    implementation("org.springframework.boot:spring-boot-starter-security")

    // redis
    implementation("org.springframework.boot:spring-boot-starter-data-redis")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")

    // aws
    implementation(platform("io.awspring.cloud:spring-cloud-aws-dependencies:3.3.1"))
    implementation("io.awspring.cloud:spring-cloud-aws-starter-ses")

    // mail
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    implementation("jakarta.mail:jakarta.mail-api")
    implementation("org.eclipse.angus:jakarta.mail")

    // testcontainers
    testImplementation("org.testcontainers:testcontainers:$testcontainersVersion")
}