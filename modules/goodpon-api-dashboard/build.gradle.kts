tasks.getByName("bootJar") {
    enabled = true
}

tasks.getByName("jar") {
    enabled = false
}

dependencies {
    implementation(project(":modules:goodpon-core"))
    implementation(project(":modules:goodpon-infra-db"))
    implementation(project(":modules:goodpon-infra-common"))
    implementation(project(":modules:goodpon-infra-security"))
    implementation(project(":modules:goodpon-support-logging"))

    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-security")

    // logging
    implementation("io.micrometer:micrometer-tracing-bridge-brave")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
}
