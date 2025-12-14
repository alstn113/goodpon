dependencies {
    implementation("org.springframework.boot:spring-boot-starter")

    implementation(project(":apps:dashboard:api"))
    implementation(project(":apps:dashboard:scheduler"))
    runtimeOnly(project(":apps:dashboard:infra"))
    implementation(project(":supports:logging"))

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation(libs.rest.assured)
    testImplementation(project(":domain"))
    testImplementation(project(":apps:dashboard:application"))
    testImplementation(project(":apps:dashboard:api"))
    testImplementation(project(":apps:dashboard:infra"))
    testImplementation(project(":infra:jpa"))
    testImplementation(project(":infra:flyway"))
    testImplementation(project(":infra:redis"))
    testImplementation(project(":infra:kafka"))

    testImplementation(testFixtures(project(":infra:jpa")))
    testImplementation(testFixtures(project(":infra:redis")))
}

tasks.bootJar {
    enabled = true
}

tasks.jar {
    enabled = false
}