dependencies {
    implementation("org.springframework.boot:spring-boot-starter")
    implementation(project(":apps:partner:api"))
    runtimeOnly(project(":apps:partner:infra"))
    implementation(project(":supports:logging"))

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation(libs.rest.assured)

    testImplementation("org.springframework.boot:spring-boot-starter-data-jpa")
    testImplementation(project(":domain"))
    testImplementation(project(":apps:partner:application"))
    testImplementation(project(":infra:jpa"))
    testImplementation(project(":infra:flyway"))
    testImplementation(project(":infra:redis"))
    testImplementation(project(":infra:kafka"))
    testImplementation(testFixtures(project(":infra:jpa")))
    testImplementation(testFixtures(project(":infra:redis")))
    testImplementation(testFixtures(project(":infra:kafka")))
}

tasks.bootJar {
    enabled = true
}

tasks.jar {
    enabled = false
}