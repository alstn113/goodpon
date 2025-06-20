tasks.getByName("bootJar") {
    enabled = true
}

tasks.getByName("jar") {
    enabled = false
}

dependencies {
    implementation(project(":goodpon-core"))
    implementation(project(":goodpon-infra-db"))
    implementation(project(":goodpon-infra-common"))
    implementation(project(":goodpon-infra-security"))
    implementation(project(":goodpon-support-logging"))
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-security")
}
