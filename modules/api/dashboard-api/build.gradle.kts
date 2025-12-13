plugins {
    alias(libs.plugins.epage.restdocs)
}

dependencies {
    runtimeOnly(project(":modules:infra:db:jpa"))
    runtimeOnly(project(":modules:infra:db:flyway"))
    runtimeOnly(project(":modules:infra:aws:ses"))
    runtimeOnly(project(":modules:infra:redis"))
    runtimeOnly(project(":modules:infra:auth"))
    runtimeOnly(project(":modules:infra:kafka"))

    implementation(project(":modules:domain"))
    implementation(project(":modules:application:dashboard-application"))
    implementation(project(":modules:support:logging"))
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-security")

    testImplementation(project(":modules:infra:db:jpa"))
    testImplementation(project(":modules:infra:db:flyway"))
    testImplementation(project(":modules:infra:aws:ses"))
    testImplementation(project(":modules:infra:redis"))
    testImplementation(project(":modules:infra:auth"))
    testImplementation(project(":modules:infra:kafka"))
    testImplementation("org.springframework.restdocs:spring-restdocs-mockmvc")
    testImplementation("org.springframework.security:spring-security-test")
    testImplementation(libs.epage.restdocs.mockmvc)
    testImplementation(libs.rest.assured)
    testImplementation(testFixtures(project(":modules:infra:db:jpa")))
    testImplementation(testFixtures(project(":modules:infra:redis")))
}

openapi3 {
    this.setServer("https://dashboard-api.goodpon.site")
    title = "Goodpon Dashboard API"
    description = "Goodpon Dashboard API Documentation"
    version = "1.0.0"
    format = "yaml"
}

tasks.bootJar {
    enabled = true
}

tasks.jar {
    enabled = false
}