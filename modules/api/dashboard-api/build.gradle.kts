tasks.getByName("bootJar") {
    enabled = true
}

tasks.getByName("jar") {
    enabled = false
}

dependencies {
    implementation(project(":modules:domain"))
    implementation(project(":modules:application:dashboard-application"))
    implementation(project(":modules:support:logging"))

    runtimeOnly(project(":modules:infra:jpa"))
    runtimeOnly(project(":modules:infra:aws:ses"))
    runtimeOnly(project(":modules:infra:redis"))
    runtimeOnly(project(":modules:infra:auth"))

    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-security")

    testImplementation(testFixtures(project(":modules:infra:jpa")))
    testImplementation(testFixtures(project(":modules:infra:redis")))
}
