tasks.getByName("bootJar") {
    enabled = true
}

tasks.getByName("jar") {
    enabled = false
}

dependencies {
    implementation(project(":modules:core"))
    implementation(project(":modules:infra:jpa"))
    implementation(project(":modules:infra:aws"))
    implementation(project(":modules:infra:redis"))
    implementation(project(":modules:infra:security"))
    implementation(project(":modules:support:logging"))

    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-security")
}
