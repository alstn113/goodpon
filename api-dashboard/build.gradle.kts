tasks.getByName("bootJar") {
    enabled = true
}

tasks.getByName("jar") {
    enabled = false
}

dependencies {
    implementation(project(":core"))
    implementation(project(":infra:jpa"))
    implementation(project(":infra:common"))
    implementation(project(":infra:security"))
    implementation(project(":support:logging"))

    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-security")
}
